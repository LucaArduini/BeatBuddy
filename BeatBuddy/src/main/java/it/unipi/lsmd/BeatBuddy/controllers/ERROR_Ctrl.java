package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ERROR_Ctrl {

    @GetMapping("/youMustBeLogged")
    public String youMustBeLogged(
            HttpSession session){
        if(!Utility.isLogged(session))
            return "error/youMustBeLogged";
        else
            return "redirect:/";
    }

    @GetMapping("/albumNotFound")
    public String albumNotFound(){
        return "error/albumNotFound";
    }
}
