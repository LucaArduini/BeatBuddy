package it.unipi.lsmd.BeatBuddy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginPage_Controller {

    @RequestMapping("/login")
    public String loginPage(){
        return "login";
    }
}
