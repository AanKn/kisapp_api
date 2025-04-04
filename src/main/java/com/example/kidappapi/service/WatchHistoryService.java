package com.example.kidappapi.service;

import com.example.kidappapi.model.dto.WatchHistoryDTO;
import com.example.kidappapi.model.entity.WatchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface WatchHistoryService {
    
    // 保存或更新观看历史
    WatchHistory saveWatchHistory(WatchHistory watchHistory);
    
    // 根据ID查找观看历史
    Optional<WatchHistory> findWatchHistoryById(Integer id);
    
    // 获取用户的所有观看历史（包含视频信息）
    List<WatchHistoryDTO> getUserWatchHistory(Integer userId);
    
    // 分页获取用户观看历史（包含视频信息）
    Page<WatchHistoryDTO> getUserWatchHistoryPaged(Integer userId, Pageable pageable);
    
    // 获取用户的最近观看历史
    List<WatchHistoryDTO> getUserRecentWatchHistory(Integer userId);
    
    // 检查用户是否已观看过该视频
    boolean hasUserWatchedVideo(Integer userId, Integer videoId);
    
    // 获取用户观看视频的进度
    Integer getUserVideoProgress(Integer userId, Integer videoId);
    
    // 更新用户观看视频的进度
    WatchHistory updateWatchProgress(Integer userId, Integer videoId, Integer progress);
    
    // 删除观看历史
    void deleteWatchHistory(Integer id);
    
    // 删除用户观看特定视频的历史
    void deleteUserVideoWatchHistory(Integer userId, Integer videoId);
    
    // 获取用户观看的不同视频数量
    long getWatchedVideosCount(Integer userId);
} 