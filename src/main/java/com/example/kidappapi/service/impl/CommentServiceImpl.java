package com.example.kidappapi.service.impl;

import com.example.kidappapi.model.dto.CommentDTO;
import com.example.kidappapi.model.entity.Comment;
import com.example.kidappapi.model.entity.User;
import com.example.kidappapi.repository.CommentRepository;
import com.example.kidappapi.repository.UserRepository;
import com.example.kidappapi.service.CommentService;
import com.example.kidappapi.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final VideoService videoService;

    @Autowired
    public CommentServiceImpl(
            CommentRepository commentRepository,
            UserRepository userRepository,
            VideoService videoService) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.videoService = videoService;
    }

    @Override
    @Transactional
    public Comment saveComment(Comment comment) {
        // 保存评论
        Comment savedComment = commentRepository.save(comment);
        
        // 增加视频评论数
        videoService.incrementCommentsCount(comment.getVideoId());
        
        return savedComment;
    }

    @Override
    public Optional<Comment> findCommentById(Integer id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<CommentDTO> getVideoComments(Integer videoId) {
        // 获取视频的所有评论
        List<Comment> comments = commentRepository.findByVideoIdOrderByCreatedAtDesc(videoId);
        
        // 将评论转换为带有用户信息的DTO
        return comments.stream()
                .map(this::convertToCommentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CommentDTO> getVideoCommentsPaged(Integer videoId, Pageable pageable) {
        // 分页获取视频评论
        Page<Comment> commentPage = commentRepository.findByVideoId(videoId, pageable);
        
        // 将评论转换为带有用户信息的DTO
        List<CommentDTO> commentDTOs = commentPage.getContent().stream()
                .map(this::convertToCommentDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(commentDTOs, pageable, commentPage.getTotalElements());
    }

    @Override
    @Transactional
    public void deleteComment(Integer id) {
        // 获取评论
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            
            // 删除评论
            commentRepository.deleteById(id);
            
            // 减少视频评论数
            videoService.decrementCommentsCount(comment.getVideoId());
        }
    }

    @Override
    public Comment updateComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getUserComments(Integer userId) {
        return commentRepository.findByUserId(userId);
    }

    @Override
    public long getVideoCommentCount(Integer videoId) {
        return commentRepository.countByVideoId(videoId);
    }
    
    // 将Comment转换为CommentDTO，包含用户信息
    private CommentDTO convertToCommentDTO(Comment comment) {
        // 获取用户信息
        Optional<User> userOptional = userRepository.findById(Long.valueOf(comment.getUserId()));
        
        String nickname = "未知用户";
        String avatarUrl = "";
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            nickname = user.getNickname() != null ? user.getNickname() : user.getUsername();
            avatarUrl = user.getAvatarUrl();
        }
        
        // 创建并返回DTO
        return new CommentDTO(comment, nickname, avatarUrl);
    }
} 