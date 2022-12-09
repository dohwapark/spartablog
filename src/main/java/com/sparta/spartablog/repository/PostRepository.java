package com.sparta.spartablog.repository;


import com.sparta.spartablog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
//    List<Post> findAllByOrderByModifiedAtDesc();
    List<Post> findAllByOrderByCreatedAtDesc();
}