package com.sparta.spartablog.dto;

import com.sparta.spartablog.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private Long commentId;
    private Long postId;
    private String comment;
    private Long userId;

    public CommentResponseDto(Comment comment){

        this.commentId = comment.getId();
        this.postId = comment.getPost().getId();
        this.comment = comment.getComment_content();
        this.userId = comment.getUser().getId();

    }

}
