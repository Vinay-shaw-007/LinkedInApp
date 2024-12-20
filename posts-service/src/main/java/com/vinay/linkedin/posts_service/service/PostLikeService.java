package com.vinay.linkedin.posts_service.service;

import com.vinay.linkedin.posts_service.auth.UserContextHolder;
import com.vinay.linkedin.posts_service.entity.Post;
import com.vinay.linkedin.posts_service.entity.PostLike;
import com.vinay.linkedin.posts_service.event.PostLikedEvent;
import com.vinay.linkedin.posts_service.exception.BadRequestException;
import com.vinay.linkedin.posts_service.exception.ResourceNotFoundException;
import com.vinay.linkedin.posts_service.repository.PostLikeRepository;
import com.vinay.linkedin.posts_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final KafkaTemplate<Long, PostLikedEvent> kafkaTemplate;

    public void likePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();

        log.info("Attempting to like the post with id: {}", postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: "+postId));

        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);

        if (alreadyLiked) throw new BadRequestException("Cannot like the same post again.");

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepository.save(postLike);

        log.info("Post with id: {} liked successfully", postId);

        PostLikedEvent postLikedEvent = PostLikedEvent.builder()
                .postId(postId)
                .likedByUserId(userId)
                .creatorId(post.getUserId())
                .build();

        kafkaTemplate.send("post-liked-topic", postId, postLikedEvent);

    }

    public void unLikePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();

        log.info("Attempting to unlike the post with id: {}", postId);

        boolean isExists = postRepository.existsById(postId);

        if (!isExists) throw new ResourceNotFoundException("Post not found with ID: "+postId);

        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);

        if (!alreadyLiked) throw new BadRequestException("Cannot un-like the post which is not liked.");

        postLikeRepository.deleteByUserIdAndPostId(userId,postId);

        log.info("Post with id: {} unliked successfully", postId);
    }
}
