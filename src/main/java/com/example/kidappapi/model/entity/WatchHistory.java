package com.example.kidappapi.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 观看历史实体类，对应数据库中的watch_history表
 */
@Entity
@Table(name = "watch_history")
public class WatchHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "video_id", nullable = false)
    private Integer videoId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "watched_at", nullable = false, updatable = true)
    private Date watchedAt;

    @Column(name = "progress", nullable = true)
    private Integer progress;

    // 默认构造函数
    public WatchHistory() {
        this.progress = 0;
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

    @PrePersist
    protected void onCreate() {
        watchedAt = new Date();
        if (progress == null) {
            progress = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        watchedAt = new Date(); // 更新时间戳
    }

    @Override
    public String toString() {
        return "WatchHistory{" +
                "id=" + id +
                ", userId=" + userId +
                ", videoId=" + videoId +
                ", watchedAt=" + watchedAt +
                ", progress=" + progress +
                '}';
    }
} 