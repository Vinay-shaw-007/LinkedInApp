package com.vinay.linkedin.posts_service.service;

import com.vinay.linkedin.posts_service.auth.UserContextHolder;
import com.vinay.linkedin.posts_service.clients.ConnectionsClient;
import com.vinay.linkedin.posts_service.dto.PersonDto;
import com.vinay.linkedin.posts_service.dto.PostCreateRequestDto;
import com.vinay.linkedin.posts_service.dto.PostDto;
import com.vinay.linkedin.posts_service.entity.Post;
import com.vinay.linkedin.posts_service.event.PostCreatedEvent;
import com.vinay.linkedin.posts_service.exception.ResourceNotFoundException;
import com.vinay.linkedin.posts_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;

    public PostDto createPost(PostCreateRequestDto postDto) {
        Long userId = UserContextHolder.getCurrentUserId();
        Post post = modelMapper.map(postDto, Post.class);
        post.setUserId(userId);

        Post savedPost = postRepository.save(post);
        PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                .postId(savedPost.getId())
                .creatorId(userId)
                .content(savedPost.getContent())
                .build();

        kafkaTemplate.send("post-created-topic", postCreatedEvent);

        return modelMapper.map(savedPost, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.debug("Retrieving post with ID: {}", postId);
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
