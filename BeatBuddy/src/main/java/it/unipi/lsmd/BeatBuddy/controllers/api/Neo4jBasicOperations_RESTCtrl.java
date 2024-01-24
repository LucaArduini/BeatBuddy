package it.unipi.lsmd.BeatBuddy.controllers.api;

import it.unipi.lsmd.BeatBuddy.repository.User_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Neo4jBasicOperations_RESTCtrl {

    @Autowired
    User_Repo user_Repo;

    @PostMapping("/api/addFollow")
    public @ResponseBody String addFollow(
            @RequestParam("user1") String user1,
            @RequestParam("user2") String user2) {

        if(user_Repo.addFollow(user1, user2))
            return "{\"outcome_code\": 0}";
        else
            return "{\"outcome_code\": 1}";
    }

    @PostMapping("/api/removeFollow")
    public @ResponseBody String removeFollow(
            @RequestParam("user1") String user1,
            @RequestParam("user2") String user2) {

        if(user_Repo.removeFollow(user1, user2))
            return "{\"outcome_code\": 0}";
        else
            return "{\"outcome_code\": 1}";
    }

    @PostMapping("/api/addLikesAbum")
    public @ResponseBody String addLikesAbum(
            @RequestParam("username") String username,
            @RequestParam("coverURL") String coverURL) {

        if(user_Repo.addLikes_A(username, coverURL))
            return "{\"outcome_code\": 0}";
        else
            return "{\"outcome_code\": 1}";
    }    
    
    @PostMapping("/api/addLikesSong")
    public @ResponseBody String addLikesSong(
            @RequestParam("username") String username,
            @RequestParam("title") String title,
            @RequestParam("coverURL") String coverURL) {

        if(user_Repo.addLikes_S(username, title, coverURL))
            return "{\"outcome_code\": 0}";
        else
            return "{\"outcome_code\": 1}";
    }

    @PostMapping("/api/albumDetails/removeLikesAlbum")
    public @ResponseBody String removeLikesAlbum(
            @RequestParam("username") String username,
            @RequestParam("coverURL") String coverURL) {

        if(user_Repo.removeLikes_A(username, coverURL))
            return "{\"outcome_code\": 0}";
        else
            return "{\"outcome_code\": 1}";
    }

    @PostMapping("/api/albumDetails/removeLikesSong")
    public @ResponseBody String removeLikesSong(
            @RequestParam("username") String username,
            @RequestParam("title") String title,
            @RequestParam("coverURL") String coverURL) {

        if(user_Repo.removeLikes_S(username, title, coverURL))
            return "{\"outcome_code\": 0}";
        else
            return "{\"outcome_code\": 1}";
    }
}
