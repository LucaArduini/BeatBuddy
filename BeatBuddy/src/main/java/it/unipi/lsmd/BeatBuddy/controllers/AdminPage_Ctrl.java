package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminPage_Ctrl {

    @RequestMapping("/adminThings")
    public String adminPage(HttpSession session) {
        if (Utility.isAdmin(session)) {
            return "adminThings";
        } else {
            return "test/accessDenied";     //###
        }
    }
}