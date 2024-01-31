package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.model.Artist;
import it.unipi.lsmd.BeatBuddy.repository.Artist_Repo_MongoDB;
import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArtistDetailsPage_Ctrl {

    @Autowired
    Artist_Repo_MongoDB artist_RepoMongoDB;

    @GetMapping("/artistDetails")
    public String artistDetails(HttpSession session,
                                Model model,
                                @RequestParam("artistId") String artistId) {
        Artist artist;

        if(artistId != null){
            artist = artist_RepoMongoDB.getArtistById(artistId);
            if(artist == null)
                return "error/artistNotFound";
        }
        else{
            return "error/artistNotFound";
        }

        model.addAttribute("logged", (Utility.isLogged(session)) ? true : false);
        model.addAttribute("artistDetails", artist);
        model.addAttribute("admin", (Utility.isAdmin(session)) ? true : false);

        return "artist";
    }
}