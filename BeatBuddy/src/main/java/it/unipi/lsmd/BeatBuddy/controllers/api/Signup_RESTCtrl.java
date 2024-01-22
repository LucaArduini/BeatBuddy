package it.unipi.lsmd.BeatBuddy.controllers.api;

import it.unipi.lsmd.BeatBuddy.repository.User_Repo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Signup_RESTCtrl {
    private static final Logger logger = LoggerFactory.getLogger(Login_RESTCtrl.class);

    @Autowired
    User_Repo user_Repo;

    @PostMapping("/api/signup")
    public @ResponseBody String signup(
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("birthday") String birthday,
            @RequestParam("email") String email) {

        logger.info("Signup attempt from user: " + username);

        try {
            int outcome = user_Repo.insertUser(name, surname, username, password, birthday, email);
            return "{\"outcome_code\": " + outcome + "}";
        } catch (Exception e) {
            logger.error("Errore durante la registrazione dell'utente", e);
            return "{\"outcome_code\": 5}"; // Errore generico non gestito
        }
    }
}
