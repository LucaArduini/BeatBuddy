package it.unipi.lsmd.BeatBuddy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DiscoverPage_Ctrl {

    @RequestMapping("/discoverPage")
    public String discoverPage(){
        return "discoverPage";
    }
}