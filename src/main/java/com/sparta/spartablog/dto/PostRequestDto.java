package com.sparta.spartablog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto {

    private String username;
    private String contents;
    private String title;
    private String password;

}
