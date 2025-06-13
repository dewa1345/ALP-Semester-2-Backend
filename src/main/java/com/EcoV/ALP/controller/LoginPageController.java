package com.EcoV.ALP.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginPageController {

    @GetMapping("/custom-login")
    public String loginPage() {
        return "login"; // assuming templates/login.html
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; // dummy page after login
    }
}
