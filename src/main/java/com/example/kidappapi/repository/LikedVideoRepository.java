package com.example.kidappapi.repository;

import com.example.kidappapi.model.entity.LikedVideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikedVideoRepository extends JpaRepository<LikedVideo, Integer> {
    // 检查用户是否已点赞视频
    boolean existsByUserIdAndVideoId(Integer userId, Integer videoId);
    
    // 获取用户点赞的所有视频ID
    Page<LikedVideo> findByUserId(Integer userId, Pageable pageable);
    
    // 删除用户的点赞记录
    void deleteByUserIdAndVideoId(Integer userId, Integer videoId);
} 