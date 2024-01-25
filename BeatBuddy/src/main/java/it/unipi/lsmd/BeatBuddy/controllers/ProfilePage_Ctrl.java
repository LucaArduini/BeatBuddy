package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.repository.User_Repo_MongoDB;
import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import it.unipi.lsmd.BeatBuddy.model.User;
import java.util.Optional;

@Controller
public class ProfilePage_Ctrl {

    @Autowired
    User_Repo_MongoDB user_Repo;

    @RequestMapping("/profilePage")
    public String profilePage(HttpSession session,
                              Model model){

        model.addAttribute("logged", (Utility.isLogged(session)) ? true : false);

        if(!Utility.isLogged(session))
            return "redirect:/login";
        else if(Utility.isAdmin(session))
            return "redirect:/adminPage";
        else{
            User optionalUser = user_Repo.getUserByUsername(Utility.getUsername(session));
            model.addAttribute("userDetails", optionalUser);
            return "profilePage";
        }
    }
}
