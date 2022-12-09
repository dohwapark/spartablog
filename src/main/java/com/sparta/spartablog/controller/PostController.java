package com.sparta.spartablog.controller;


import com.sparta.spartablog.dto.PostRequestDto;
import com.sparta.spartablog.entity.Post;
import com.sparta.spartablog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

//    @GetMapping("/")
//    public ModelAndView home() {
//        return new ModelAndView("index");
//    }

    @PostMapping("/posts")
    public Post createPost(@RequestBody PostRequestDto requestDto) {
        return postService.createPost(requestDto);
    }
    // 메모 생성하기

    @GetMapping("/posts")
    public List<Post> getPosts() {

        return postService.getPosts();
    }
    // 메모 조회하기

    @GetMapping("/posts/{id}")
    public Optional<Post> getPost(@PathVariable Long id){

        return postService.getPost(id);
    }
    // 메모 상세조회


    @PutMapping("/posts/{id}")
    public Long updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {
        return postService.update(id, requestDto);
    }
    // 메모 수정하기

    @DeleteMapping("/posts/{id}")
    public Long deletePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {
        return postService.deletePost(id, requestDto);
    }
    // 메모 삭제하기

}