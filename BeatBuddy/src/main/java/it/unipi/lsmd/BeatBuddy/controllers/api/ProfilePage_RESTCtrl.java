package it.unipi.lsmd.BeatBuddy.controllers.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ProfilePage_RESTCtrl {
    private static final Logger logger = LoggerFactory.getLogger(Login_RESTCtrl.class);

    @PostMapping("/api/logout")
    public @ResponseBody String logout(HttpSession session) {
        logger.info("Logout attempt from user: " + session.getAttribute("username"));
        //Gson gson = new Gson();
        if (!Utility.isLogged(session)) {
            // Lancia un'eccezione se l'utente non è loggato
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Accesso non autorizzato");
        }

        session.invalidate(); // Invalida la sessione se l'utente è loggato
        logger.info("User logged out");
        return "{\"outcome_code\": 0}";
    }
}