package it.unipi.lsmd.BeatBuddy.controllers.api;

import com.google.gson.Gson;
import it.unipi.lsmd.BeatBuddy.repository.User_Repo_Neo4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ProfilePage_RESTCtrl {
    private static final Logger logger = LoggerFactory.getLogger(ProfilePage_RESTCtrl.class);

    @Autowired
    private User_Repo_Neo4j user_Repo_Neo4j;

    @PostMapping("/api/logout")
    public @ResponseBody String logout(HttpSession session) {
        logger.info("Logout attempt from user: " + session.getAttribute("username"));
        if (!Utility.isLogged(session)) {
            // Lancia un'eccezione se l'utente non è loggato
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Accesso non autorizzato");
        }

        session.invalidate(); // Invalida la sessione se l'utente è loggato
        logger.info("User logged out");
        return "{\"outcome_code\": 0}";
    }

    @GetMapping("/api/userLikedSongs")
    public @ResponseBody String userLikedSongs( HttpSession session,
                                                @RequestParam("username") String username) {
        if (!Utility.isLogged(session)) {
            return "{\"outcome_code\": 1}"; // User not found
        }
        if (Utility.isAdmin(session)) {
            return "{\"outcome_code\": 2}"; // feature not accessible for admins
        }

        return new Gson().toJson(user_Repo_Neo4j.getLikedSongsByUsername(username));
    }

    @GetMapping("/api/userLikedAlbums")
    public @ResponseBody String userLikedAlbums(HttpSession session,
                                                 @RequestParam("username") String username) {
        if (!Utility.isLogged(session)) {
            return "{\"outcome_code\": 1}"; // User not found
        }
        if (Utility.isAdmin(session)) {
            return "{\"outcome_code\": 2}"; // feature not accessible for admins
        }

        return new Gson().toJson(user_Repo_Neo4j.getLikedAlbumsByUsername(username));
    }

    @GetMapping("/api/userFollowedUsers")
    public @ResponseBody String userFollowedUsers( HttpSession session,
                                                   @RequestParam("username") String username) {
        if (!Utility.isLogged(session)) {
            return "{\"outcome_code\": 1}"; // User not found
        }

        System.out.println(user_Repo_Neo4j.getFollowedUsersByUsername(username));
        return new Gson().toJson(user_Repo_Neo4j.getFollowedUsersByUsername(username));
    }
}