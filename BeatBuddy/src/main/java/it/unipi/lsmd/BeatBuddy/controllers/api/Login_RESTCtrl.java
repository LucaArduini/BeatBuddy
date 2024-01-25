package it.unipi.lsmd.BeatBuddy.controllers.api;

import it.unipi.lsmd.BeatBuddy.model.User;
import it.unipi.lsmd.BeatBuddy.repository.User_Repo_MongoDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.web.bind.annotation.*;
import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;

import java.util.Optional;

@RestController
public class Login_RESTCtrl {
    private static final Logger logger = LoggerFactory.getLogger(Login_RESTCtrl.class);

    @Autowired
    User_Repo_MongoDB user_RepoMongoDB;

    @PostMapping("/api/login")
    public @ResponseBody String login(HttpSession session,
                                      @RequestParam("username") String username,
                                      @RequestParam("password") String password) {
        logger.info("Login attempt from user: " + username);

        try {
            User user = user_RepoMongoDB.getUserByUsername(username);

            if(user == null){
                return "{\"outcome_code\": 1}";     // User not found
            }

            String hashedPassword = Hashing.sha256()
                    .hashString(password, StandardCharsets.UTF_8)
                    .toString();

            if(user.getPassword().equals(hashedPassword)){
                //session.setAttribute("logged", true);
                session.setAttribute("username", username);
                session.setAttribute("role", user.isAdmin() ? "admin" : "regUser");
                return "{\"outcome_code\": 0}";     // Login successful
            }
            else {
                return "{\"outcome_code\": 2}";     // Wrong password
            }

        } catch (DataAccessResourceFailureException e) {
            logger.error("Impossibile connettersi al database", e);
            return "{\"outcome_code\": 3}";         // Errore di connessione al database
        }
    }
}