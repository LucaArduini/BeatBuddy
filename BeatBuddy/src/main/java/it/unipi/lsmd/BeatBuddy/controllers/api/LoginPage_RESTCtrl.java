package it.unipi.lsmd.BeatBuddy.controllers.api;

import it.unipi.lsmd.BeatBuddy.model.User;
import it.unipi.lsmd.BeatBuddy.repository.User_Repo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;

import java.util.Optional;

@RestController
public class LoginPage_RESTCtrl {
    private static final Logger logger = LoggerFactory.getLogger(LoginPage_RESTCtrl.class);

    @Autowired
    User_Repo user_Repo;

    @PostMapping("/api/login")
    public @ResponseBody String login(HttpSession session,
                                      @RequestParam(value = "username") String username,
                                      @RequestParam(value = "password") String password) {
        logger.info("Login attempt from user: " + username);

        Optional<User> optionalUser = user_Repo.getUserByUsername(username);
        User userData = optionalUser.orElse(null);

        if(optionalUser.isEmpty())    //###
            System.out.println("User not found");
        else
            System.out.println("User found");

        if(userData != null)    //###
            logger.info(userData.toString());

        if(userData == null){
            return new Gson().toJson("{\"outcome_code\": 1}");  // User not found
        }

        String hashedPassword = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();

        if(userData.getPassword().equals(hashedPassword)){
            session.setAttribute("username", username);
            session.setAttribute("role", userData.isAdmin() ? "admin" : "regUser");

            return new Gson().toJson("{\"outcome_code\": 0}");  // Login successful
        }
        else {
            return new Gson().toJson("{\"outcome_code\": 2}");  // Wrong password
        }
    }
}