package com.sparta.spartablog.controller;

import com.sparta.spartablog.dto.LoginRequestDto;
import com.sparta.spartablog.dto.ResponseDto;
import com.sparta.spartablog.dto.SignupRequestDto;
import com.sparta.spartablog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    // 회원 가입 페이지
    @GetMapping("/signup")
    public ModelAndView signupPage() {

        return new ModelAndView("signup");
    }

    // 로그인 페이지
    @GetMapping("/login")
    public ModelAndView loginPage() {

        return new ModelAndView("login");
    }

    // 회원가입
    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
//        return "redirect:/api/user/login";
        return "signup success";
    }



    // 로그인
    @ResponseBody
    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        return "login success";
    }

}