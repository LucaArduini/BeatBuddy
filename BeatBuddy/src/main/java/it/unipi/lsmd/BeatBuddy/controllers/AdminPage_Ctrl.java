package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminPage_Ctrl {

    @RequestMapping("/adminPage")
    public String adminPage(HttpSession session) {
<<<<<<< Updated upstream
        if (Utility.isAdmin(session)) {
            return "test/accessAllowed";
        } else {
            return "error/accessDenied";
        }
=======
        // session.setAttribute("role", "admin");

        if (!Utility.isLogged(session))
            return "error/youMustBeLogged";
        else if (Utility.isAdmin(session))
            return "test/BOTTONI_ADMIN";
        else
            return "error/accessDenied";
>>>>>>> Stashed changes
    }
}