package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminPage_Ctrl {

    @RequestMapping("/adminPage")
    public String adminPage(HttpSession session) {
//        if (!Utility.isLogged(session))
//            return "error/youMustBeLogged";
//        else if (Utility.isAdmin(session))

            session.setAttribute("role", "admin");
            return "test/BOTTONI_ADMIN";
//        else
//            return "error/accessDenied";
    }
}