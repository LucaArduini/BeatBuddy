package it.unipi.lsmd.BeatBuddy.controllers.api;

import com.google.gson.Gson;
import it.unipi.lsmd.BeatBuddy.repository.Album_Repo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Search_RESTCtrl {
    private static final Logger logger = LoggerFactory.getLogger(LoginPage_RESTCtrl.class);

    @Autowired
    Album_Repo album_Repo;

    @GetMapping("/api/search")
    public @ResponseBody String search(@RequestParam(value = "term") String term,
                                       @RequestParam(value = "category") String category){
        logger.info("Search attempt: " + term + " in " + category);

        if(category.equals("album")){
            System.out.println(new Gson().toJson(album_Repo.find5AlbumsDTO(term)));
            return new Gson().toJson(album_Repo.find5AlbumsDTO(term));
        }
        else if(category.equals("artist")){
            return "Artist search not implemented yet";
        }
        else if(category.equals("song")){
            return "Song search not implemented yet";
        }
        else if(category.equals("user")){
            return "User search not implemented yet";
        }
        else

        return "Search attempt";
    }
}
