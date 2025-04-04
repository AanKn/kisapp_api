package com.example.kidappapi.service;

import com.example.kidappapi.model.entity.LikedVideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikedVideoService {
    // 用户点赞视频
    LikedVideo likeVideo(Integer userId, Integer videoId);
    
    // 用户取消点赞视频
    void unlikeVideo(Integer userId, Integer videoId);
    
    // 检查用户是否已点赞视频
    boolean hasUserLikedVideo(Integer userId, Integer videoId);
    
    // 获取用户点赞的所有视频
    Page<LikedVideo> getUserLikedVideos(Integer userId, Pageable pageable);
} 