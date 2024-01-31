package it.unipi.lsmd.BeatBuddy.controllers;

import it.unipi.lsmd.BeatBuddy.model.Song_Neo4j;
import it.unipi.lsmd.BeatBuddy.model.User_Neo4j;
import it.unipi.lsmd.BeatBuddy.repository.Song_Repo_Neo4j;
import it.unipi.lsmd.BeatBuddy.repository.User_Repo_Neo4j;
import it.unipi.lsmd.BeatBuddy.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@Controller
public class DiscoverPage_Ctrl {

    @Autowired
    Song_Repo_Neo4j song_RepoNeo4j;
    @Autowired
    User_Repo_Neo4j user_RepoNeo4j;

    @RequestMapping("/discoverPage")
    public String discoverPage(HttpSession session,
                               Model model){

        if(!Utility.isLogged(session))
            return "redirect:/login";
        if(Utility.isAdmin(session))
            return "error/accessDenied";

        String currentUsername = Utility.getUsername(session);

        ArrayList<Song_Neo4j> suggestedSongs_ByTaste = song_RepoNeo4j.getSuggestedSongs_ByTaste(currentUsername);
        if(suggestedSongs_ByTaste == null)
            return "error/genericError";
        else{
            if(suggestedSongs_ByTaste.isEmpty())
                model.addAttribute("no_s_1", true);
            else
                model.addAttribute("suggestedSongs_ByTaste", suggestedSongs_ByTaste);
        }

        ArrayList<Song_Neo4j> suggestedSongs_ByFollow = song_RepoNeo4j.getSuggestedSongs_ByFollowed(currentUsername);
        if(suggestedSongs_ByFollow == null)
            return "error/genericError";
        else {
            if (suggestedSongs_ByFollow.isEmpty())
                model.addAttribute("no_s_2", true);
            else
                model.addAttribute("suggestedSongs_ByFollow", suggestedSongs_ByFollow);
        }

        ArrayList<User_Neo4j> suggestedUsersToFollow = user_RepoNeo4j.findSuggestedUserstoFollow(currentUsername);
        if(suggestedUsersToFollow == null)
            return "error/genericError";
        else {
            if (suggestedSongs_ByFollow.isEmpty())
                model.addAttribute("no_s_3", true);
            else
                model.addAttribute("suggestedUsersToFollow", suggestedUsersToFollow);
        }

        model.addAttribute("logged", (Utility.isLogged(session)) ? true : false);

        if(!suggestedSongs_ByTaste.isEmpty() || !suggestedSongs_ByFollow.isEmpty() || !suggestedUsersToFollow.isEmpty())
            return "discoverPage";
        else
            return "error/nothingToDiscover";
    }
}