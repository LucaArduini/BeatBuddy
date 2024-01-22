package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.model.Album;
import it.unipi.lsmd.BeatBuddy.repository.Album_Repo;
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
public class AlbumDetailsPage_Ctrl {
    private static final Logger logger = LoggerFactory.getLogger(AlbumDetailsPage_Ctrl.class);

    @Autowired
    Album_Repo album_Repo;

    @GetMapping("/albumDetails")
    public String albumDetails(HttpSession session,
                               Model model,
                               @RequestParam(name = "albumId") String albumId) {

        Optional<Album> optionalAlbum = album_Repo.getAlbumById(albumId);
        //Album albumData = optionalAlbum.orElse(null);

        model.addAttribute("albumFound", (optionalAlbum.isEmpty()) ? false : true);
        model.addAttribute("logged", (Utility.isLogged(session)) ? true : false);

        if(!optionalAlbum.isEmpty())
            model.addAttribute("albumDetails", optionalAlbum.get());

        return "album"; // Il nome della vista (ad esempio, una pagina Thymeleaf chiamata albumPage.html)
    }
}