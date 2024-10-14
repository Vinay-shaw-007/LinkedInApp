package com.vinay.linkedin.posts_service.controller;

import com.vinay.linkedin.posts_service.auth.UserContextHolder;
import com.vinay.linkedin.posts_service.dto.PostCreateRequestDto;
import com.vinay.linkedin.posts_service.dto.PostDto;
import com.vinay.linkedin.posts_service.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/core")
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping()
    public ResponseEntity<PostDto> createPost(
                                            @RequestBody PostCreateRequestDto post,
                                            HttpServletRequest httpServletRequest) {
        PostDto postDto = postService.createPost(post, 1L);
        return new ResponseEntity<>(postDto, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        PostDto postDto = postService.getPostById(postId);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/users/{userId}/allPosts")
    public ResponseEntity<List<PostDto>> getAllPostsOfUser(@PathVariable Long userId) {
        List<PostDto> allPosts = postService.getAllPostsOfUser(userId);
        return ResponseEntity.ok(allPosts);
    }
}
