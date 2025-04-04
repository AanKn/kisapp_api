package com.example.kidappapi.service;

import com.example.kidappapi.model.dto.CommentDTO;
import com.example.kidappapi.model.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    
    // 保存评论
    Comment saveComment(Comment comment);
    
    // 根据ID查找评论
    Optional<Comment> findCommentById(Integer id);
    
    // 获取视频的所有评论（包含用户信息）
    List<CommentDTO> getVideoComments(Integer videoId);
    
    // 分页获取视频评论（包含用户信息）
    Page<CommentDTO> getVideoCommentsPaged(Integer videoId, Pageable pageable);
    
    // 删除评论
    void deleteComment(Integer id);
    
    // 更新评论内容
    Comment updateComment(Comment comment);
    
    // 获取用户的所有评论
    List<Comment> getUserComments(Integer userId);
    
    // 获取视频评论数
    long getVideoCommentCount(Integer videoId);
} 