package com.sparta.spartablog.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Setter
@Getter
public class SignupRequestDto {
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "username은 알파벳 소문자와 숫자로 구성된 4~10자로 구성")
    private String username;
    @Pattern(regexp = "^[A-Za-z0-9]{8,15}$", message = "password는 알파벳 대소문자와 숫자로 구성된 8~15자로 구성")
    private String password;

//    private String username;
//    private String password;
    private String email;
    private boolean admin = false;
    private String adminToken = "";
}