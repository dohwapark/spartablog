package com.sparta.spartablog.service;


import com.sparta.spartablog.dto.PostRequestDto;
import com.sparta.spartablog.entity.Post;
import com.sparta.spartablog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Post createPost(PostRequestDto requestDto) {
        Post post = new Post(requestDto);
        postRepository.save(post);
        return post;
    }
    // 메모 생성하기

    @Transactional
    public List<Post> getPosts() {

//        return postRepository.findAllByOrderByModifiedAtDesc();
        return postRepository.findAllByOrderByCreatedAtDesc();
    }
    // 메모 조회하기

    @Transactional
    public Optional<Post> getPost(Long id){
        return postRepository.findById(id);
    }
    // 메모 상세조회

    @Transactional
    public Long update(Long id, PostRequestDto requestDto) {
//        Memo post = memoRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
//        );
//
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            System.out.println("post.get().getPassword() : " + post.get().getPassword() + " requestDto.getPassword() : " + requestDto.getPassword());
            if (post.get().getPassword().equals(requestDto.getPassword())) {
                Post post1 = postRepository.findById(id).orElseThrow(
                        () -> new NullPointerException("존재하지 않는 아이디입니다.")
                );
                post1.update(requestDto);
            } else {
                System.out.println("비밀번호 오류");
                return 0L;
            }
        }
        return id;
    }
    // 메모 수정하기

    @Transactional
    public Long deletePost(Long id, PostRequestDto requestDto) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            System.out.println("post.get().getPassword() : " + post.get().getPassword() + " requestDto.getPassword() : " + requestDto.getPassword());
            if (post.get().getPassword().equals(requestDto.getPassword())) {
                postRepository.deleteById(id);
            } else {
                System.out.println("비밀번호 오류");
                return 0L;
            }
        }
        return id;
    }
    // 메모 삭제하기

}