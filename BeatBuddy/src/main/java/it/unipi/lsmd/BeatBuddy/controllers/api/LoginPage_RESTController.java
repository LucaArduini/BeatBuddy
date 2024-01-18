package it.unipi.lsmd.BeatBuddy.controllers.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginPage_RESTController {
    private static final Logger logger = LoggerFactory.getLogger(LoginPage_RESTController.class);

    @PostMapping("/api/login")
    public @ResponseBody String login(HttpSession session,
                                        @RequestParam(value = "username") String username,
                                        @RequestParam(value = "password") String password) {
        logger.info("Login attempt from user: " + username);
        //Gson gson = new Gson();

        if(username.equals("admin") && password.equals("admin")){
            session.setAttribute("username", "admin");
            session.setAttribute("role", "admin");
            return new Gson().toJson("{\"outcome_code\": 0}");
        }
        else if(username.equals("user") && password.equals("user")){
            session.setAttribute("username", "user");
            session.setAttribute("role", "regUser");
            return new Gson().toJson("{\"outcome_code\": 0}");
        }
        else
            return new Gson().toJson("{\"outcome_code\": 2}");
    }
}