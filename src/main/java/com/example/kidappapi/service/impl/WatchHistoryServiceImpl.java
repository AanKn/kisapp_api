package com.example.kidappapi.service.impl;

import com.example.kidappapi.model.dto.WatchHistoryDTO;
import com.example.kidappapi.model.entity.Video;
import com.example.kidappapi.model.entity.WatchHistory;
import com.example.kidappapi.repository.WatchHistoryRepository;
import com.example.kidappapi.service.VideoService;
import com.example.kidappapi.service.WatchHistoryService;
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
public class WatchHistoryServiceImpl implements WatchHistoryService {

    private final WatchHistoryRepository watchHistoryRepository;
    private final VideoService videoService;

    @Autowired
    public WatchHistoryServiceImpl(
            WatchHistoryRepository watchHistoryRepository,
            VideoService videoService) {
        this.watchHistoryRepository = watchHistoryRepository;
        this.videoService = videoService;
    }

    @Override
    @Transactional
    public WatchHistory saveWatchHistory(WatchHistory watchHistory) {
        // 检查是否已存在该用户观看该视频的记录
        Optional<WatchHistory> existingRecord = watchHistoryRepository.findByUserIdAndVideoId(
                watchHistory.getUserId(), watchHistory.getVideoId());
        
        if (existingRecord.isPresent()) {
            // 更新已有记录
            WatchHistory record = existingRecord.get();
            record.setProgress(watchHistory.getProgress());
            // watchedAt会通过@PreUpdate自动更新
            return watchHistoryRepository.save(record);
        } else {
            // 创建新记录
            return watchHistoryRepository.save(watchHistory);
        }
    }

    @Override
    public Optional<WatchHistory> findWatchHistoryById(Integer id) {
        return watchHistoryRepository.findById(id);
    }

    @Override
    public List<WatchHistoryDTO> getUserWatchHistory(Integer userId) {
        // 获取用户的所有观看历史
        List<WatchHistory> watchHistoryList = watchHistoryRepository.findByUserIdOrderByWatchedAtDesc(userId);
        
        // 将观看历史转换为带有视频信息的DTO
        return watchHistoryList.stream()
                .map(this::convertToWatchHistoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<WatchHistoryDTO> getUserWatchHistoryPaged(Integer userId, Pageable pageable) {
        // 分页获取用户观看历史
        Page<WatchHistory> watchHistoryPage = watchHistoryRepository.findByUserId(userId, pageable);
        
        // 将观看历史转换为带有视频信息的DTO
        List<WatchHistoryDTO> watchHistoryDTOs = watchHistoryPage.getContent().stream()
                .map(this::convertToWatchHistoryDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(watchHistoryDTOs, pageable, watchHistoryPage.getTotalElements());
    }

    @Override
    public List<WatchHistoryDTO> getUserRecentWatchHistory(Integer userId) {
        // 获取用户最近7天的观看历史
        List<WatchHistory> recentHistory = watchHistoryRepository.findUserRecentWatchHistory(userId);
        
        // 将观看历史转换为带有视频信息的DTO
        return recentHistory.stream()
                .map(this::convertToWatchHistoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasUserWatchedVideo(Integer userId, Integer videoId) {
        return watchHistoryRepository.findByUserIdAndVideoId(userId, videoId).isPresent();
    }

    @Override
    public Integer getUserVideoProgress(Integer userId, Integer videoId) {
        Optional<WatchHistory> watchHistory = watchHistoryRepository.findByUserIdAndVideoId(userId, videoId);
        return watchHistory.map(WatchHistory::getProgress).orElse(0);
    }

    @Override
    @Transactional
    public WatchHistory updateWatchProgress(Integer userId, Integer videoId, Integer progress) {
        Optional<WatchHistory> watchHistoryOptional = watchHistoryRepository.findByUserIdAndVideoId(userId, videoId);
        
        WatchHistory watchHistory;
        if (watchHistoryOptional.isPresent()) {
            // 更新已有记录
            watchHistory = watchHistoryOptional.get();
            watchHistory.setProgress(progress);
        } else {
            // 创建新记录
            watchHistory = new WatchHistory();
            watchHistory.setUserId(userId);
            watchHistory.setVideoId(videoId);
            watchHistory.setProgress(progress);
        }
        
        return watchHistoryRepository.save(watchHistory);
    }

    @Override
    @Transactional
    public void deleteWatchHistory(Integer id) {
        watchHistoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteUserVideoWatchHistory(Integer userId, Integer videoId) {
        watchHistoryRepository.deleteByUserIdAndVideoId(userId, videoId);
    }

    @Override
    public long getWatchedVideosCount(Integer userId) {
        return watchHistoryRepository.countDistinctVideosByUserId(userId);
    }
    
    // 将WatchHistory转换为WatchHistoryDTO，包含视频信息
    private WatchHistoryDTO convertToWatchHistoryDTO(WatchHistory watchHistory) {
        // 获取视频信息
        Optional<Video> videoOptional = videoService.findVideoById(watchHistory.getVideoId());
        
        Video video = null;
        if (videoOptional.isPresent()) {
            video = videoOptional.get();
        }
        
        // 创建并返回DTO
        return new WatchHistoryDTO(watchHistory, video);
    }
} 