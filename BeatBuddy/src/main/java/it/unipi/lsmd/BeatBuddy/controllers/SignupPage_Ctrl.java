package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class SignupPage_Ctrl {

    @RequestMapping("/signup")
    public String discoverPage(HttpSession session,
                               Model model){

        model.addAttribute("logged", (Utility.isLogged(session)) ? true : false);

        if(Utility.isLogged(session))
            return "errror/alreadyLogged";
        else
            return "signup";
    }
}