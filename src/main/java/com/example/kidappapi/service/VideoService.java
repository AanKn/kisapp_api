package com.example.kidappapi.service;

import com.example.kidappapi.model.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * 视频服务接口
 */
public interface VideoService {
    
    /**
     * 保存视频
     */
    Video saveVideo(Video video);
    
    /**
     * 根据ID查找视频
     */
    Optional<Video> findVideoById(Integer id);
    
    /**
     * 查找所有视频
     */
    Page<Video> findAllVideos(Pageable pageable);
    
    /**
     * 根据类型查找视频
     */
    Page<Video> findVideosByType(Video.VideoType type, Pageable pageable);
    
    /**
     * 根据标题搜索视频
     */
    Page<Video> searchVideosByTitle(String title, Pageable pageable);
    
    /**
     * 根据类型和标题搜索视频
     */
    Page<Video> searchVideosByTypeAndTitle(Video.VideoType type, String title, Pageable pageable);
    
    /**
     * 查找热门视频
     */
    Page<Video> findHotVideos(Pageable pageable);
    
    /**
     * 查找最新视频
     */
    Page<Video> findLatestVideos(Pageable pageable);
    
    /**
     * 根据类型查找热门视频
     */
    Page<Video> findHotVideosByType(Video.VideoType type, Pageable pageable);
    
    /**
     * 根据类型查找最新视频
     */
    Page<Video> findLatestVideosByType(Video.VideoType type, Pageable pageable);
    
    /**
     * 更新视频信息
     */
    Video updateVideo(Video video);
    
    /**
     * 删除视频
     */
    void deleteVideo(Integer id);
    
    /**
     * 增加视频点赞数
     */
    void incrementLikesCount(Integer videoId);
    
    /**
     * 减少视频点赞数
     */
    void decrementLikesCount(Integer videoId);
    
    /**
     * 增加视频评论数
     */
    void incrementCommentsCount(Integer videoId);
    
    /**
     * 减少视频评论数
     */
    void decrementCommentsCount(Integer videoId);
} 