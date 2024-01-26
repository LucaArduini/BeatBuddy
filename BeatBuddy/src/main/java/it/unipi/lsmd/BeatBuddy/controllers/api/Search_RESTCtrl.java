package it.unipi.lsmd.BeatBuddy.controllers.api;

import com.google.gson.Gson;
import it.unipi.lsmd.BeatBuddy.repository.Album_Repo;
import it.unipi.lsmd.BeatBuddy.repository.Album_Repo_MongoDB;
import it.unipi.lsmd.BeatBuddy.repository.Artist_Repo;
import it.unipi.lsmd.BeatBuddy.repository.User_Repo_MongoDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Search_RESTCtrl {
    private static final Logger logger = LoggerFactory.getLogger(Login_RESTCtrl.class);

    @Autowired
    Album_Repo_MongoDB album_RepoMongoDB;

    @Autowired
    Artist_Repo artist_Repo;

    @Autowired
    User_Repo_MongoDB user_RepoMongoDB;

    @GetMapping("/api/search")
    public @ResponseBody String search(@RequestParam("term") String term,
                                       @RequestParam("category") String category){
        logger.info("Search attempt: " + term + " in " + category);

        if(category.equals("album")){
            return new Gson().toJson(album_RepoMongoDB.find5AlbumsDTO(term));
        }
        else if(category.equals("artist")){
            return new Gson().toJson(artist_Repo.find5ArtistsDTO(term));
        }
        else if(category.equals("song")){
            return new Gson().toJson(album_RepoMongoDB.find5SongsDTO(term));
        }
        else if(category.equals("user")){
            return new Gson().toJson(user_RepoMongoDB.find5UserDTO(term));
        }
        else
            return "Invalid category";
    }
}
