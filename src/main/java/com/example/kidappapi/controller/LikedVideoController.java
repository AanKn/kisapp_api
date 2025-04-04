package com.example.kidappapi.controller;

import com.example.kidappapi.model.dto.ApiResponse;
import com.example.kidappapi.model.entity.LikedVideo;
import com.example.kidappapi.service.LikedVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/liked-videos")
public class LikedVideoController {

    private final LikedVideoService likedVideoService;

    @Autowired
    public LikedVideoController(LikedVideoService likedVideoService) {
        this.likedVideoService = likedVideoService;
    }

    /**
     * 用户点赞视频
     */
    @PostMapping("/users/{userId}/videos/{videoId}")
    public ResponseEntity<ApiResponse<LikedVideo>> likeVideo(
            @PathVariable Integer userId,
            @PathVariable Integer videoId) {
        try {
            LikedVideo likedVideo = likedVideoService.likeVideo(userId, videoId);
            return ResponseEntity.ok(ApiResponse.success("点赞成功", likedVideo));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * 用户取消点赞视频
     */
    @DeleteMapping("/users/{userId}/videos/{videoId}")
    public ResponseEntity<ApiResponse<Void>> unlikeVideo(
            @PathVariable Integer userId,
            @PathVariable Integer videoId) {
        try {
            likedVideoService.unlikeVideo(userId, videoId);
            return ResponseEntity.ok(ApiResponse.success("取消点赞成功", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * 检查用户是否已点赞视频
     */
    @GetMapping("/users/{userId}/videos/{videoId}/check")
    public ResponseEntity<ApiResponse<Boolean>> checkUserLikedVideo(
            @PathVariable Integer userId,
            @PathVariable Integer videoId) {
        boolean hasLiked = likedVideoService.hasUserLikedVideo(userId, videoId);
        return ResponseEntity.ok(ApiResponse.success(hasLiked));
    }

    /**
     * 获取用户点赞的所有视频
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Page<LikedVideo>>> getUserLikedVideos(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<LikedVideo> likedVideos = likedVideoService.getUserLikedVideos(
                userId, PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success(likedVideos));
    }
} 