package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DiscoverPage_Ctrl {

    @RequestMapping("/discoverPage")
    public String discoverPage(HttpSession session,
                               Model model){
        model.addAttribute("logged", (Utility.isLogged(session)) ? true : false);

        if(!Utility.isLogged(session))
            return "redirect:/login";
        else
            return "discoverPage";
    }
}