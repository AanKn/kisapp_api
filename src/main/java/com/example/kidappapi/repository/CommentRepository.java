package com.example.kidappapi.repository;

import com.example.kidappapi.model.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    
    // 根据视频ID查询评论，按创建时间降序排序
    List<Comment> findByVideoIdOrderByCreatedAtDesc(Integer videoId);
    
    // 分页查询视频评论
    Page<Comment> findByVideoId(Integer videoId, Pageable pageable);
    
    // 根据用户ID查询评论
    List<Comment> findByUserId(Integer userId);
    
    // 根据视频ID统计评论数
    long countByVideoId(Integer videoId);
} 