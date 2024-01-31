package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.repository.Album_Repo_MongoDB;
import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WriteReviewPage_Ctrl {

    @Autowired
    Album_Repo_MongoDB album_RepoMongoDB;

    /**
     * Displays the page for writing a review for a specific album.
     * This method checks user authentication and album existence before rendering the write review page.
     *
     * @param session The HttpSession to check if the user is logged in.
     * @param model The Spring MVC model to store attributes for the view.
     * @param albumId The unique identifier of the album for which the review is being written.
     * @return The view name to be displayed (writeReview) or error views based on authentication and album existence.
     */
    @GetMapping("/writeReview")
    public String writeReview(HttpSession session,
                           Model model,
                           @RequestParam("albumId") String albumId) {

        if(!Utility.isLogged(session))
            return "error/youMustBeLogged";
        if(Utility.isAdmin(session))
            return "error/accessDenied";

        boolean albumFound = album_RepoMongoDB.existsById(albumId);
        if(albumFound)
            model.addAttribute("albumId", albumId);
        else
            return "error/albumNotFound";

        model.addAttribute("logged", Utility.isLogged(session));
        model.addAttribute("isAdmin", Utility.isAdmin(session));

        if(Utility.isLogged(session))
            return "writeReview";
        else
            return "error/youMustBeLogged";
    }
}
