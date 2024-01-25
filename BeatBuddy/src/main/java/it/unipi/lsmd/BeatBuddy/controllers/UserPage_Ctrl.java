package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.model.Review;
import it.unipi.lsmd.BeatBuddy.model.ReviewedAlbum;
import it.unipi.lsmd.BeatBuddy.model.User;
import it.unipi.lsmd.BeatBuddy.repository.User_Repo_MongoDB;
import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserPage_Ctrl {

    @Autowired
    User_Repo_MongoDB user_RepoMongoDB;

    @RequestMapping("/user")
    public String discoverPage(HttpSession session,
                               Model model,
                               @RequestParam("username") String username){
        User user;

        if(username != null){
            user = user_RepoMongoDB.getUserByUsername(username);
            if(user == null)
                return "error/userNotFound";
        }else{
            return "error/userNotFound";
        }

        model.addAttribute("logged", (Utility.isLogged(session)) ? true : false);

        if(Utility.isLogged(session)){
            if(!optionalUser.isEmpty()){
                model.addAttribute("userDetails", optionalUser.get());
                /*List<ReviewedAlbum> allReviews = List.of(optionalUser.get().getReviewedAlbums());
                List<ReviewedAlbum> firstFiveReviews = allReviews.subList(0, 5);
                model.addAttribute("reviewedAlbums", firstFiveReviews);*/
                return "user";
            }else
                return "error/userNotFound";
        }else
            return "error/youMustBeLogged";
    }
}