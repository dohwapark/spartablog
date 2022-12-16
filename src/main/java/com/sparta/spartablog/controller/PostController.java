package com.sparta.spartablog.controller;


import com.sparta.spartablog.dto.PostRequestDto;
import com.sparta.spartablog.dto.PostResponseDto;
import com.sparta.spartablog.dto.ResponseDto;
import com.sparta.spartablog.entity.Post;
import com.sparta.spartablog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

//    @GetMapping("/")
//    public ModelAndView home() {
//        return new ModelAndView("index");
//    }

    @PostMapping("/posts")
    public Post createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        return postService.createPost(requestDto, request);
    }
    // 메모 생성하기

    @GetMapping("/posts")
    public List<PostResponseDto> getPosts() {

        return postService.getPosts();
    }
    // 메모 조회하기

    @GetMapping("/posts/{id}")
    public Optional<Post> getPost(@PathVariable Long id){

        return postService.getPost(id);
    }
    // 메모 상세조회


    @PutMapping("/posts/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        return postService.update(id, requestDto, request);
    }
    // 메모 수정하기

    @DeleteMapping("posts/{id}")
    public ResponseDto deletePost(@PathVariable Long id, HttpServletRequest request) {
        return postService.deletePost(id, request);
    }
    // 메모 삭제하기

}