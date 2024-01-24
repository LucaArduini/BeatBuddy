package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.model.User;
import it.unipi.lsmd.BeatBuddy.repository.User_Repo;
import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class UserPage_Ctrl {

    @Autowired
    User_Repo user_Repo;

    @RequestMapping("/user")
    public String discoverPage(HttpSession session,
                               Model model,
                               @RequestParam(required = false) String username){
        Optional<User> optionalUser;

        if(username != null){
            optionalUser = user_Repo.getUserByUsername(username);
        }else{
            return "error/userNotFound";
        }

        model.addAttribute("logged", (Utility.isLogged(session)) ? true : false);

        if(Utility.isLogged(session)){
            if(!optionalUser.isEmpty()){
                model.addAttribute("userDetails", optionalUser.get());
                return "user";
            }else
                return "error/userNotFound";
        }else
            return "error/youMustBeLogged";
    }
}