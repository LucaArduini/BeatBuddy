package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
public class MostPopularPage_Ctrl {

    @RequestMapping("/mostPopularPage")
    public String discoverPage(HttpSession session,
                               Model model,
                               @RequestParam("type") String type){
        if(!Objects.equals(type, "artists") && !Objects.equals(type, "albums") && !Objects.equals(type, "songs"))
            return "redirect:/login";
        else {
            model.addAttribute("logged", (Utility.isLogged(session)) ? true : false);
            model.addAttribute("type", type);

            return "mostPopularPage";
        }
    }
}