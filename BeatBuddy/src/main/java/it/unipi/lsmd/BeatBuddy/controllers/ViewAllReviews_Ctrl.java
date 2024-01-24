package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.model.Review;
import it.unipi.lsmd.BeatBuddy.repository.Album_Repo;
import it.unipi.lsmd.BeatBuddy.repository.Review_Repo;
import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ViewAllReviews_Ctrl {

    @Autowired
    Album_Repo album_Repo;
    @Autowired
    Review_Repo review_Repo;

    @GetMapping("/viewAllReviews")
    public String viewAllReviews(HttpSession session,
                                 Model model,
                                 @RequestParam("albumId") String albumId) {

        boolean albumFound = album_Repo.existsById(albumId);
        if(albumFound){
            List<Review> reviews = review_Repo.getReviewsByAlbumID(albumId);
            boolean reviewsFound = (reviews != null && !reviews.isEmpty());

            if (reviewsFound){
                model.addAttribute("reviews", reviews);
            }
            else {
                model.addAttribute("albumId", albumId);
                return "error/noReviewsFound";
            }
        }
        else{
            return "albumNotFound";
        }

        model.addAttribute("logged", (Utility.isLogged(session)) ? true : false);

        return "test/viewAllReviews";   //### LORE modifica questa riga
    }
}
