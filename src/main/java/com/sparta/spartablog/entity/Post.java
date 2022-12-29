package com.sparta.spartablog.entity;

import com.sparta.spartablog.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;


@Getter // lombok이 해당 필드에 대한 기본 getter를 생성
@Entity // 테이블을 나타냄
@NoArgsConstructor // 파라미터가 없는 기본생성자를 대신 생성
// Post post1 = new Post(); --> @NoArgsConstructor
// Post post2 = new Post("user2", "contents2", "title2"); --> @RequiredArgsConstructor
// final 이나 @NonNull인 필드 값만 받는 생성자를 만듦
// Post post3 = new Post("3L", "user3", "contents3", "title3", "user2"); --> @AllArgsConstructor
// 모든 필드 값을 파라미터로 받는 생성자를 만듦
public class Post extends Timestamped {
    @Id // ID 값, PK로 사용하겠다는 뜻
    @GeneratedValue(strategy = GenerationType.AUTO) // 자동 증가
    private Long id;
    @Column(nullable = false) // 반드시 존재해야 함
    private String username;
    @Column(nullable = false)
    private String contents;
    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER) // 다대일
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Comment> comments;

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

    public void update(PostRequestDto requestDto) {
        this.contents = requestDto.getContents();
        this.title = requestDto.getTitle();
    }


}