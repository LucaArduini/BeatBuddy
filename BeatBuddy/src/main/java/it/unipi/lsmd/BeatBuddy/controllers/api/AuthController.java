package it.unipi.lsmd.BeatBuddy.controllers.api;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.*;
//import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder; ###

@RestController
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/api/login")
    public @ResponseBody String login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        logger.info("Login attempt from user: " + username);
        return new Gson().toJson("{\"type\": 0, \"message\" : \"OK\"}");
    }
}