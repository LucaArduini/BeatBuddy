package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.repository.Album_Repo;
import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WriteReviewPage_Ctrl {

    @Autowired
    Album_Repo album_Repo;

    @GetMapping("/writeReview")
    public String homePage(HttpSession session,
                           Model model,
                           @RequestParam("albumId") String albumId) {

        boolean albumFound = album_Repo.existsById(albumId);
        if(albumFound)
            model.addAttribute("albumId", albumId);
        else
            return "albumNotFound";

        model.addAttribute("logged", (Utility.isLogged(session)) ? true : false);

        if(Utility.isLogged(session))
            return "writeReview";
        else
            return "redirect:/youMustBeLogged";
    }
}
