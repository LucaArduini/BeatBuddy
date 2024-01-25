package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.model.Album;
import it.unipi.lsmd.BeatBuddy.repository.Album_Repo_MongoDB;
import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AlbumDetailsPage_Ctrl {
    private static final Logger logger = LoggerFactory.getLogger(ArtistDetailsPage_Ctrl.class);

    @Autowired
    Album_Repo_MongoDB album_RepoMongoDB;

    @GetMapping("/albumDetails")
    public String albumDetails(HttpSession session,
                               Model model,
                               @RequestParam(required = false) String albumId,
                               @RequestParam(required = false) String artist,
                               @RequestParam(required = false) String title) {
        Album album;

        if (albumId != null) {
            // Logica per gestire la richiesta basata su albumId
            album = album_RepoMongoDB.getAlbumById(albumId);
        }
        else if (artist != null && title != null) {
            // Logica per gestire la richiesta basata su artist e title
            album = album_RepoMongoDB.getAlbumByTitleAndArtist(title, artist);
        }
        else {
            // Caso in cui nessuno dei parametri è fornito
            return "error/albumNotFound";
        }

        if(album == null)
            return "error/albumNotFound";
        else {
            //calcolo le durate delle canzoni in min e sec
            album.calculateDurations_MinSec();

            model.addAttribute("albumDetails", album);
            model.addAttribute("albumTotalDuration", album.calculateTotalDuration());
        }

        model.addAttribute("logged", (Utility.isLogged(session)) ? true : false);
      
        return "albumDetails";
    }
}