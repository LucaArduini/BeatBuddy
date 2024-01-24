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

import java.util.List;
import java.util.Optional;

@Controller
public class AlbumDetailsPage_Ctrl {
    private static final Logger logger = LoggerFactory.getLogger(ArtistDetailsPage_Ctrl.class);

    @Autowired
    Album_Repo album_Repo;

    @GetMapping("/albumDetails")
    public String albumDetails(HttpSession session, Model model,
                               @RequestParam(required = false) String albumId,
                               @RequestParam(required = false) String artist,
                               @RequestParam(required = false) String title) {
        Optional<Album> optionalAlbum;

        if (albumId != null) {
            // Logica per gestire la richiesta basata su albumId
            optionalAlbum = album_Repo.getAlbumById(albumId);
        }
        else if (artist != null && title != null) {
            // Logica per gestire la richiesta basata su artist e title
            List<Album> list = album_Repo.getAlbumsByTitleAndArtist(title, artist);

            if(list.size() == 1)
                optionalAlbum = Optional.of(list.get(0));
            else if(list.size() > 1) {
                // Se ci sono più album con lo stesso titolo e artista, prendo il primo
                optionalAlbum = Optional.of(list.get(0));
                logger.warn("Multiple albums found with the same title and artist");
            }
            else{
                // Se la lista è vuota
                optionalAlbum = Optional.empty();
            }
        }
        else {
            // Caso in cui nessuno dei parametri è fornito
            return "albumNotFound";
        }

        if(optionalAlbum.isEmpty())
            return "albumNotFound";
        else {
            Album tmp_album = optionalAlbum.get();
            //calcolo le durate delle canzoni in min e sec
            tmp_album.calculateDurations_MinSec();

            model.addAttribute("albumDetails", tmp_album);
            model.addAttribute("albumTotalDuration", tmp_album.calculateTotalDuration());
        }

        model.addAttribute("logged", (Utility.isLogged(session)) ? true : false);
      
        return "albumDetails";
    }
}