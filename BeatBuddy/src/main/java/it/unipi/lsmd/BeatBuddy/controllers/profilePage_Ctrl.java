package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class profilePage_Ctrl {

    @RequestMapping("/profilePage")
    public String profilePage(HttpSession session){
        if(!Utility.isLogged(session))
            return "redirect:/login";
        else
            return "profilePage";
    }
}
