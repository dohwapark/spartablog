package com.sparta.spartablog.repository;

import com.sparta.spartablog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByOrderByModifiedAtDesc();
    List<Comment> findAllByPost_IdOrderByModifiedAtDesc(Long post_id);


}
