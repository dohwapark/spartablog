package com.sparta.spartablog.entity;

import com.sparta.spartablog.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment_content;



    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;



    public Comment(Post post, CommentRequestDto requestDto, User user)
    {
        this.post = post;
        this.comment_content = requestDto.getComment();
        this.user = user;
    }

    public void update(CommentRequestDto requestDto) {
        this.comment_content = requestDto.getComment();
    }
}
