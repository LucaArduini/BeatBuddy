package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.model.Artist;
import it.unipi.lsmd.BeatBuddy.repository.Artist_Repo;
import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ArtistDetailsPage_Ctrl {
    private static final Logger logger = LoggerFactory.getLogger(ArtistDetailsPage_Ctrl.class);

    @Autowired
    Artist_Repo artist_Repo;

    @GetMapping("/artistDetails")
    public String artistDetails(HttpSession session, Model model,
                               @RequestParam(name = "artistId") String artistId) {

        Optional<Artist> optionalArtist = artist_Repo.getArtistById(artistId);
        //Album albumData = optionalAlbum.orElse(null);

        model.addAttribute("artistFound", (optionalArtist.isEmpty()) ? false : true);
        model.addAttribute("logged", (Utility.isLogged(session)) ? true : false);

        if(!optionalArtist.isEmpty())
            model.addAttribute("artistDetails", optionalArtist.get());

        return "artist"; // Il nome della vista (ad esempio, una pagina Thymeleaf chiamata albumPage.html)
    }
}