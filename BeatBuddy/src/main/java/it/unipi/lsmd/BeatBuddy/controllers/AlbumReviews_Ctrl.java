package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.model.Review;
import it.unipi.lsmd.BeatBuddy.repository.Album_Repo_MongoDB;
import it.unipi.lsmd.BeatBuddy.repository.Review_Repo_MongoDB;
import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;

@Controller
public class AlbumReviews_Ctrl {

    @Autowired
    Album_Repo_MongoDB album_RepoMongoDB;
    @Autowired
    Review_Repo_MongoDB review_RepoNeo4j;

    @GetMapping("/albumReviews")
    public String albumReviews(HttpSession session,
                                 Model model,
                                 @RequestParam("albumId") String albumId) {

        boolean albumFound = album_RepoMongoDB.existsById(albumId);
        if(albumFound){
            List<Review> reviewList = review_RepoNeo4j.getReviewsByAlbumID(albumId);
            LinkedList<Review> reviews = new LinkedList<>(reviewList);
            boolean reviewsFound = (reviews != null && !reviews.isEmpty());
            if (reviewsFound){

                // if the user has already reviewed the album, put his review as first in the list
                if(Utility.isLogged(session) && !Utility.isAdmin(session)){
                    String username = (String) session.getAttribute("username");
                    for (Review review : reviews) {
                        if(review.getUsername().equals(username)){
                            reviews.remove(review);
                            reviews.add(0, review);
                            break;
                        }
                    }
                }

                for (Review review : reviews) {
                    review.setPrintableDate();
                }
                model.addAttribute("reviews", reviews);
            }
            else {
                model.addAttribute("albumId", albumId);
                return "error/noReviewsFound";
            }
        }
        else{
            return "error/albumNotFound";
        }

        model.addAttribute("logged", (Utility.isLogged(session)) ? true : false);
        model.addAttribute("admin", (Utility.isAdmin(session)) ? true : false);

        return "albumReviews";
    }
}
