package com.example.kidappapi.model.dto;

import com.example.kidappapi.model.entity.Comment;
import java.util.Date;

/**
 * 评论数据传输对象，包含评论内容和用户信息
 */
public class CommentDTO {
    private Integer id;
    private Integer videoId;
    private Integer userId;
    private String content;
    private Date createdAt;
    private String userNickname;
    private String userAvatarUrl;

    public CommentDTO() {
    }

    // 根据Comment实体和用户信息构建DTO
    public CommentDTO(Comment comment, String userNickname, String userAvatarUrl) {
        this.id = comment.getId();
        this.videoId = comment.getVideoId();
        this.userId = comment.getUserId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.userNickname = userNickname;
        this.userAvatarUrl = userAvatarUrl;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }
} 