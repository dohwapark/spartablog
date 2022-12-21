package com.sparta.spartablog.service;

import com.sparta.spartablog.dto.*;
import com.sparta.spartablog.entity.Post;
import com.sparta.spartablog.entity.User;
import com.sparta.spartablog.entity.UserRoleEnum;
import com.sparta.spartablog.entity.Comment;
import com.sparta.spartablog.jwt.JwtUtil;
import com.sparta.spartablog.repository.PostRepository;
import com.sparta.spartablog.repository.UserRepository;
import com.sparta.spartablog.repository.EmailRepository;
import com.sparta.spartablog.repository.CommentRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);  //bearer 부분 떼고 암호화된 부분만 token에 넣음
        Claims claims;

        // 토큰이 있는 경우에만 게시글 작성
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);  // 토큰에서 사용자 정보 가져오기
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            //DB에 저장할 Board 객체 만들기
//            Board boardPost = new Board(requestDto, user.getUsername(), user.getId());
//            boardRepository.saveAndFlush(boardPost);

            //DB에 저장할 Board 객체 만들기

            Post post = new Post(requestDto, user);
            postRepository.saveAndFlush(post);

//            Post post = postRepository.saveAndFlush(new Post(requestDto, user.getId()));

            return new PostResponseDto(post);
        }
        else {
            return null;
        }
    }
//        Post post = new Post(requestDto);
//        postRepository.save(post);
//        return post;
//    }

    // 메모 생성하기




    @Transactional
    public List<PostResponseDto> getPosts() {

        List<PostResponseDto> postListResponseDto = new ArrayList<>();
        List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc(); //레포지토리에 수정날짜순으로 가져오게 작성했

        for(int i = 0 ; i < postList.size() ; i++){

            PostResponseDto postResponseDto = new PostResponseDto(postList.get(i)); //


            postListResponseDto.add(postResponseDto);
        } return postListResponseDto;
    }
    // 메모 조회하기

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id) {

        Post post = checkPost(id);
        System.out.println("board = " + post);

        PostResponseDto postResponseDto = new PostResponseDto(post);

        List<Comment> commentList = commentRepository.findAllByPost_IdOrderByModifiedAtDesc(id);
//        List<String> commentContentList = commentRepository.findAllByBoard_IdOrderByModifiedAtDesc(id).getComment_content;

        for (Comment comment : commentList) {
            postResponseDto.getComments().add(new CommentResponseDto(comment));
        }
        return postResponseDto ;

    }
    // 메모 상세조회



    @Transactional
    public Long update(Long id, PostRequestDto requestDto, HttpServletRequest request) {
//        Memo post = memoRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
//        );
        String token = jwtUtil.resolveToken(request); //token에 bearer부분 떼고 담기(토큰 유효 검사위함)
        Claims claims;   //토큰 안에 있는 user 정보 담기 위함
        Optional<Post> post = postRepository.findById(id);

        if (token != null) {
            if (jwtUtil.validateToken(token)) {       //토큰 유효한지 검증

                claims = jwtUtil.getUserInfoFromToken(token); // 토큰에서 사용자 정보(body에 있는) 가져오기
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회("sub"부분에 있는 username을 가지고옴)
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            UserRoleEnum role = user.getRole();
            System.out.println("role = " + role);

            System.out.println("claims.getSubject() : " + claims.getSubject());

            Post post1 = postRepository.findById(id).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 아이디입니다.")
            );
            //            Post post = checkPost(id);
            if (post1.getUsername().equals(user.getUsername()) || role == UserRoleEnum.ADMIN) {
                post1.update(requestDto);
            } else {
                System.out.println("게시글 삭제 권한이 없습니다.");
                return 0L;
            }


        } else {
            throw new IllegalArgumentException("게시글 수정 권한이 없습니다.");
        }

        return id;
    }
    // 메모 수정하기

    @Transactional
    public Long deletePost(Long id, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        Claims claims;


        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            UserRoleEnum role = user.getRole();

            Post post = checkPost(id);
//            if (post.getUsername().equals(claims.getSubject()) || role == UserRoleEnum.ADMIN) {
//                postRepository.deleteById(id);
//            }
//            return new ResponseDto("게시물 삭제 성공", HttpStatus.OK.value());
            if (post.getUsername().equals(claims.getSubject()) || role == UserRoleEnum.ADMIN) {
                postRepository.deleteById(id);
            } else {
                System.out.println("게시글 삭제 권한이 없습니다.");
                return 0L;
            }
//            return new ResponseDto("게시물 삭제 성공", HttpStatus.OK.value());

        } else {
            throw new IllegalArgumentException("게시글 삭제 권한이 없습니다.");
        }
        // 메모 삭제하기
        return id;
    }

    private Post checkPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new RuntimeException("게시물이 존재하지 않습니다"));
        return post;
    }
    //**코멘트 작성**
    public CommentResponseDto createComment (@PathVariable Long id, @RequestBody CommentRequestDto
            requestDto, HttpServletRequest request){

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("유효한 토큰이 아닙니다.");
            }

            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            System.out.println("username = " + user.getUsername());


            Post post = checkPost(id);
            Comment commentPost = new Comment(post, requestDto, user);
            commentRepository.saveAndFlush(commentPost);

//                return new BoardCommentResponseDto(commentPost, board);

            return new CommentResponseDto(commentPost);
        }

        throw new IllegalArgumentException("토큰이 없습니다");


    }


    //**코멘트 수정**
    @Transactional
    public CommentResponseDto updateComment (Long comment_id, CommentRequestDto requestDto, HttpServletRequest
            request){

        String token = jwtUtil.resolveToken(request); //token에 bearer부분 떼고 담기(토큰 유효 검사위함)
        Claims claims;   //토큰 안에 있는 user 정보 담기 위함

        // 토큰이 있는 경우에만 게시글 수정
        if (token != null) {
            if (jwtUtil.validateToken(token)) {       //토큰 유효한지 검증

                claims = jwtUtil.getUserInfoFromToken(token); // 토큰에서 사용자 정보(body에 있는) 가져오기
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회("sub"부분에 있는 username을 가지고옴)
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            UserRoleEnum role = user.getRole();
            System.out.println("role = " + role);

            System.out.println("claims.getSubject() : " + claims.getSubject());


            Comment comment = checkComment(comment_id);
            if (comment.getUser().getUsername().equals(user.getUsername()) || role == UserRoleEnum.ADMIN) {
                comment.update(requestDto);
            }

//                Board board = checkBoard(comment.getBoard().getId());
            return new CommentResponseDto(comment);

        } else {
            throw new IllegalArgumentException("댓글 수정 권한이 없습니다.");
        }

    }

    //**코멘트 삭제**
    @Transactional
    public ResponseDto deleteComment (Long comment_id, HttpServletRequest request ){

        String token = jwtUtil.resolveToken(request);
        Claims claims;


        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            UserRoleEnum role = user.getRole();

            Comment comment = checkComment(comment_id);
            if (comment.getUser().getUsername().equals(claims.getSubject()) || role == UserRoleEnum.ADMIN) {
                commentRepository.deleteById(comment_id);
            }
            return new ResponseDto("댓글 삭제 성공", HttpStatus.OK.value());

        } else {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }


    }
    // **선택한 코멘트 존재 확인&담기**
    private Comment checkComment (Long comment_id){
        Comment comment = commentRepository.findById(comment_id).orElseThrow(
                () -> new RuntimeException("댓글이 존재하지 않습니다"));
        return comment;
    }
}