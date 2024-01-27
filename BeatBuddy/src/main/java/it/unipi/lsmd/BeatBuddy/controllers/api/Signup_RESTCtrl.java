package it.unipi.lsmd.BeatBuddy.controllers.api;

import it.unipi.lsmd.BeatBuddy.repository.User_Repo_MongoDB;
import it.unipi.lsmd.BeatBuddy.repository.User_Repo_Neo4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Signup_RESTCtrl {
    private static final Logger logger = LoggerFactory.getLogger(Login_RESTCtrl.class);

    @Autowired
    User_Repo_MongoDB user_RepoMongoDB;
    @Autowired
    User_Repo_Neo4j user_RepoNeo4j;

    @PostMapping("/api/signup")
    @Transactional
    public @ResponseBody String signup(
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("birthday") String birthday,
            @RequestParam("email") String email) {

        logger.info("Signup attempt from user: " + username);

        try {
            int outcomeM = user_RepoMongoDB.insertUser(name, surname, username, password, birthday, email);
            // Se l'inserimento in MongoDB non riesce, non procedere con Neo4j
            if (outcomeM != 0) {
                return constructOutcomeResponse(outcomeM);
            }

            int outcomeN = user_RepoNeo4j.insertUser(username);
            // Se l'inserimento in MongoDB ha avuto successo ma quello in Neo4j no
            if (outcomeN != 0) {
                return "{\"outcome_code\": 6}"; // Inserimento in Neo4j fallito
            }

            return "{\"outcome_code\": 0}"; // User registered successfully

        } catch (Exception e) {
            logger.error("Errore durante la registrazione dell'utente", e);
            return "{\"outcome_code\": 5}"; // Errore generico non gestito
        }
    }

    private String constructOutcomeResponse(int outcomeCode) {
        switch (outcomeCode) {
            case 1:
                return "{\"outcome_code\": 1}"; // Username already exists
            case 2:
                return "{\"outcome_code\": 2}"; // Email already exists
            case 3:
                return "{\"outcome_code\": 3}"; // Error while connecting to MongoDB
            case 4:
                return "{\"outcome_code\": 4}"; // Other MongoDB error
            default:
                return "{\"outcome_code\": 5}"; // Unhandled error
        }
    }
}
