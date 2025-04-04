package com.example.kidappapi.model.dto;

import com.example.kidappapi.model.entity.Video;
import com.example.kidappapi.model.entity.WatchHistory;
import java.util.Date;

/**
 * 观看历史数据传输对象，包含观看历史和视频信息
 */
public class WatchHistoryDTO {
    private Integer id;
    private Integer userId;
    private Integer videoId;
    private Date watchedAt;
    private Integer progress;
    
    // 视频信息
    private String videoTitle;
    private String videoDescription;
    private String videoThumbnailUrl;
    private Integer videoDuration;
    private String videoType;

    public WatchHistoryDTO() {
    }

    // 根据WatchHistory实体和Video实体构建DTO
    public WatchHistoryDTO(WatchHistory watchHistory, Video video) {
        this.id = watchHistory.getId();
        this.userId = watchHistory.getUserId();
        this.videoId = watchHistory.getVideoId();
        this.watchedAt = watchHistory.getWatchedAt();
        this.progress = watchHistory.getProgress();
        
        if (video != null) {
            this.videoTitle = video.getTitle();
            this.videoDescription = video.getDescription();
            this.videoThumbnailUrl = video.getThumbnailUrl();
            this.videoDuration = video.getDuration();
            this.videoType = video.getType() != null ? video.getType().toString() : null;
        }
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public Date getWatchedAt() {
        return watchedAt;
    }

    public void setWatchedAt(Date watchedAt) {
        this.watchedAt = watchedAt;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public String getVideoThumbnailUrl() {
        return videoThumbnailUrl;
    }

    public void setVideoThumbnailUrl(String videoThumbnailUrl) {
        this.videoThumbnailUrl = videoThumbnailUrl;
    }

    public Integer getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(Integer videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }
} 