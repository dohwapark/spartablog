package com.sparta.spartablog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/api")
public class BlogController {

    @GetMapping("/post")
    public ModelAndView home() {
        return new ModelAndView("index");
    }
}