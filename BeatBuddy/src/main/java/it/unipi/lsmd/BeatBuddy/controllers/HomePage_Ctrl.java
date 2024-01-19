package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomePage_Ctrl {

    @RequestMapping("/homePage")
    public String homePage(HttpSession session){
        if(Utility.isLogged(session))
            return "homePage";
        else
            return "redirect:/";
    }
}
