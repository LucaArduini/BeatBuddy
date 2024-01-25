package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexPage_Ctrl {

    @RequestMapping("/")
    public String indexPage(HttpSession session){
        if(Utility.isLogged(session))
            return "redirect:/homePage";
        else
            return "index_NEW";
    }
}