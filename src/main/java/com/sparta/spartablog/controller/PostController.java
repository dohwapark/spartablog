package com.sparta.spartablog.controller;


import com.sparta.spartablog.dto.*;
import com.sparta.spartablog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;


//@Controller
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 메인 페이지
    @GetMapping("/")
    @ResponseBody
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    // 게시글 생성
    // HttpServletRequest 객체가 소멸하기 까지 상태정보를 유지할 때
    // 한번의 요청으로 실행된 페이지끼리 정보를 공유하고자 할 때 사용
    @PostMapping("/posts")
    @ResponseBody
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        return postService.createPost(requestDto, request);
    }

    // 게시글 리스트 전체 조회
    @GetMapping("/posts")
    @ResponseBody
    public List<PostResponseDto> getPosts() {

        return postService.getPosts();
    }

    // 게시글 상세 조회
    @GetMapping("/posts/detail")
    @ResponseBody
    public PostResponseDto getPost(@RequestParam Long id) {

        return postService.getPost(id);
    }


    // 게시글 수정
    // {id} 템플릿 변수
    // @PathVariable 를 이용해서 템플릿 변수와 동일한 이름을 갖는 파라미터를 추가
    @PutMapping("/posts/{id}")
    @ResponseBody
    public Long updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest request) {
//        return postService.update(id, requestDto, request);
        return postService.update(id, requestDto, request);
    }


    // 게시글 삭제
    @DeleteMapping("/posts/{id}")
    @ResponseBody
    public Long deletePost(@PathVariable Long id, HttpServletRequest request) {
        return postService.deletePost(id, request);
    }

    // 댓글 작성
    @PostMapping("/comment/{id}")
    public CommentResponseDto createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return postService.createComment(id, requestDto, request);
    }

    // 댓글 수정
    @PutMapping("/comment/{comment_id}")
    public CommentResponseDto updateComment(@PathVariable Long comment_id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return postService.updateComment(comment_id, requestDto, request);
    }


    // 댓글 삭제
    @DeleteMapping("/comment/{comment_id}")
    public ResponseDto deleteComment(@PathVariable Long comment_id, HttpServletRequest request) {
        return postService.deleteComment(comment_id, request);
    }
}