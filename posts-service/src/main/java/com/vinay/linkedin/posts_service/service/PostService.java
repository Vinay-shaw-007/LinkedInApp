package com.vinay.linkedin.posts_service.service;

import com.vinay.linkedin.posts_service.auth.UserContextHolder;
import com.vinay.linkedin.posts_service.clients.ConnectionsClient;
import com.vinay.linkedin.posts_service.dto.PersonDto;
import com.vinay.linkedin.posts_service.dto.PostCreateRequestDto;
import com.vinay.linkedin.posts_service.dto.PostDto;
import com.vinay.linkedin.posts_service.entity.Post;
import com.vinay.linkedin.posts_service.exception.ResourceNotFoundException;
import com.vinay.linkedin.posts_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ConnectionsClient connectionsClient;

    public PostDto createPost(PostCreateRequestDto postDto, Long userId) {
        Post post = modelMapper.map(postDto, Post.class);
        post.setUserId(userId);

        Post savedPost = postRepository.save(post);
        return modelMapper.map(savedPost, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.debug("Retrieving post with ID: {}", postId);
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("User Id: {}", userId);
        List<PersonDto> firstDegreeConnections = connectionsClient.getFirstDegreeConnections();
        log.info("firstDegreeConnections: {}", firstDegreeConnections);
//        TODO send notification to all the connections.
        return postRepository.findById(postId)
                .map(post -> modelMapper.map(post, PostDto.class))
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: "+postId));
    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        List<Post> allPost = postRepository.findByUserId(userId);
        return allPost
                .stream()
                .map(element -> modelMapper.map(element, PostDto.class))
                .toList();
    }
}
