package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.model.dummy.AdminStats;
import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
public class AdminPage_Ctrl {

    @RequestMapping("/adminPage")
    public String adminPage(HttpSession session,
                            Model model){
        if (!Utility.isLogged(session))
            return "error/youMustBeLogged";
        else if(!Utility.isAdmin(session))
            return "error/accessDenied";

        try {
            model.addAttribute("logged", Utility.isLogged(session));
            AdminStats adminStats = Utility.readAdminStats();
            if(adminStats != null){
                model.addAttribute("adminStatsFound", true);
                model.addAttribute("dailyLikesOnAlbums", adminStats.getDailyLikesOnAlbums());
                model.addAttribute("dailyLikesOnSongs", adminStats.getDailyLikesOnSongs());
                model.addAttribute("dailyReviews", adminStats.getDailyReviews());
            }
            else
                model.addAttribute("adminStatsFound", false);

            return "adminPage";

        } catch (Exception e) {
            e.printStackTrace();
            return "error/genericError";
        }
    }
}