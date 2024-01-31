package it.unipi.lsmd.BeatBuddy.controllers.api;

import com.google.gson.Gson;
import it.unipi.lsmd.BeatBuddy.repository.Album_Repo_MongoDB;
import it.unipi.lsmd.BeatBuddy.repository.Artist_Repo_MongoDB;
import it.unipi.lsmd.BeatBuddy.repository.User_Repo_MongoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Search_RESTCtrl {

    @Autowired
    Album_Repo_MongoDB album_RepoMongoDB;
    @Autowired
    Artist_Repo_MongoDB artist_RepoMongoDB;
    @Autowired
    User_Repo_MongoDB user_RepoMongoDB;

    /**
     * Performs a search operation based on the provided search term and category.
     * This method searches for albums, artists, songs, or users based on the specified category.
     *
     * @param term The search term to be used for the search.
     * @param category The category in which to perform the search (album, artist, song, user).
     * @return A JSON representation of search results based on the category, or "Invalid category" if an
     * invalid category is provided.
     */
    @GetMapping("/api/search")
    public @ResponseBody String search(@RequestParam("term") String term,
                                       @RequestParam("category") String category){

        if(category.equals("album")){
            return new Gson().toJson(album_RepoMongoDB.find5AlbumsDTO(term));
        }
        else if(category.equals("artist")){
            return new Gson().toJson(artist_RepoMongoDB.find5ArtistsDTO(term));
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
