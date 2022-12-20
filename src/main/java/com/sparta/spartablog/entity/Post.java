package com.sparta.spartablog.entity;

import com.sparta.spartablog.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String contents;
    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


    public Post(PostRequestDto requestDto) {
        this.contents = requestDto.getContents();
        this.title = requestDto.getTitle();

    }


    public Post(PostRequestDto requestDto, User user) {

        super();
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.username = user.getUsername();
        this.user = user;
    }
//    public Post(PostRequestDto requestDto, Long userId) {
//
//
//        this.title = requestDto.getTitle();
//        this.contents = requestDto.getContents();
//        this.id = requestDto.getId();
//        this.userId = userId;
//    }


    public void update(PostRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }


}