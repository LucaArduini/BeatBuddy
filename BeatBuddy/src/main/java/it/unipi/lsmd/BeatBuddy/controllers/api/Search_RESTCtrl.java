package it.unipi.lsmd.BeatBuddy.controllers.api;

import com.google.gson.Gson;
import it.unipi.lsmd.BeatBuddy.repository.Album_Repo;
import it.unipi.lsmd.BeatBuddy.repository.Artist_Repo;
import it.unipi.lsmd.BeatBuddy.repository.User_Repo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Search_RESTCtrl {
    private static final Logger logger = LoggerFactory.getLogger(Login_RESTCtrl.class);

    @Autowired
    Album_Repo album_Repo;

    @Autowired
    Artist_Repo artist_Repo;

    @Autowired
    User_Repo user_Repo;

    @GetMapping("/api/search")
    public @ResponseBody String search(@RequestParam("term") String term,
                                       @RequestParam("category") String category){
        logger.info("Search attempt: " + term + " in " + category);

        if(category.equals("album")){
            System.out.println(new Gson().toJson(album_Repo.find5AlbumDTO(term)));
            return new Gson().toJson(album_Repo.find5AlbumDTO(term));
        }
        else if(category.equals("artist")){
            System.out.println(new Gson().toJson(artist_Repo.find5ArtistDTO(term)));
            return new Gson().toJson(artist_Repo.find5ArtistDTO(term));
        }
        else if(category.equals("song")){
            System.out.println(new Gson().toJson(album_Repo.find5SongDTO(term)));
            return new Gson().toJson(album_Repo.find5SongDTO(term));
        }
        else if(category.equals("user")){
            System.out.println(new Gson().toJson(user_Repo.find5UserDTO(term)));
            return new Gson().toJson(user_Repo.find5UserDTO(term));
        }
        else
            return "Invalid category";
    }
}
