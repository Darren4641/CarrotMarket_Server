package com.Carrot.CR_Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/carrot")
public class ViewController {

    @GetMapping("/main")
    public String init() {
        return "/user/mainView";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "/user/login";
    }

    @GetMapping("/signupForm")
    public String signupForm() {
        return "/user/signup";
    }
}
