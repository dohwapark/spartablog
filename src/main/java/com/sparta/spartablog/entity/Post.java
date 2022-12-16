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
@Setter
@Entity
@ToString
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


    public Post(String contents, String title) {
        this.contents = contents;
        this.title = title;

    }


    public Post(PostRequestDto requestDto, User user) {
        super();
        this.username = user.getUsername();
        this.contents = requestDto.getContents();
        this.title = requestDto.getTitle();
        this.user = user;
    }

    public void update(PostRequestDto postRequestDto) {
        this.username = postRequestDto.getTitle();
        this.contents = postRequestDto.getContents();
    }


}