package com.example.kidappapi.controller;

import com.example.kidappapi.model.dto.ApiResponse;
import com.example.kidappapi.model.dto.WatchHistoryDTO;
import com.example.kidappapi.model.entity.WatchHistory;
import com.example.kidappapi.service.VideoService;
import com.example.kidappapi.service.WatchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 观看历史控制器，提供RESTful API接口
 */
@RestController
@RequestMapping("/api/watch-history")
public class WatchHistoryController {

    private final WatchHistoryService watchHistoryService;
    private final VideoService videoService;

    @Autowired
    public WatchHistoryController(WatchHistoryService watchHistoryService, VideoService videoService) {
        this.watchHistoryService = watchHistoryService;
        this.videoService = videoService;
    }

    /**
     * 记录视频观看历史
     */
    @PostMapping
    public ResponseEntity<ApiResponse<WatchHistory>> recordWatchHistory(@RequestBody WatchHistory watchHistory) {
        // 检查视频是否存在
        if (!videoService.findVideoById(watchHistory.getVideoId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "视频不存在"));
        }

        WatchHistory savedHistory = watchHistoryService.saveWatchHistory(watchHistory);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("观看历史记录成功", savedHistory));
    }

    /**
     * 获取用户观看历史（包含视频信息）
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<List<WatchHistoryDTO>>> getUserWatchHistory(@PathVariable Integer userId) {
        List<WatchHistoryDTO> watchHistory = watchHistoryService.getUserWatchHistory(userId);
        return ResponseEntity.ok(ApiResponse.success(watchHistory));
    }

    /**
     * 分页获取用户观看历史（包含视频信息）
     */
    @GetMapping("/users/{userId}/page")
    public ResponseEntity<ApiResponse<Page<WatchHistoryDTO>>> getUserWatchHistoryPaged(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // 创建分页请求，按观看时间降序排序
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "watchedAt"));
        Page<WatchHistoryDTO> watchHistoryPage = watchHistoryService.getUserWatchHistoryPaged(userId, pageRequest);
        
        return ResponseEntity.ok(ApiResponse.success(watchHistoryPage));
    }

    /**
     * 获取用户最近的观看历史
     */
    @GetMapping("/users/{userId}/recent")
    public ResponseEntity<ApiResponse<List<WatchHistoryDTO>>> getUserRecentWatchHistory(@PathVariable Integer userId) {
        List<WatchHistoryDTO> recentHistory = watchHistoryService.getUserRecentWatchHistory(userId);
        return ResponseEntity.ok(ApiResponse.success(recentHistory));
    }

    /**
     * 获取观看历史详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WatchHistory>> getWatchHistoryById(@PathVariable Integer id) {
        Optional<WatchHistory> watchHistory = watchHistoryService.findWatchHistoryById(id);
        if (watchHistory.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(watchHistory.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "观看历史记录不存在"));
        }
    }

    /**
     * 检查用户是否观看过视频
     */
    @GetMapping("/users/{userId}/videos/{videoId}/check")
    public ResponseEntity<ApiResponse<Boolean>> checkUserWatchedVideo(
            @PathVariable Integer userId,
            @PathVariable Integer videoId) {
        boolean hasWatched = watchHistoryService.hasUserWatchedVideo(userId, videoId);
        return ResponseEntity.ok(ApiResponse.success(hasWatched));
    }

    /**
     * 获取用户观看视频的进度
     */
    @GetMapping("/users/{userId}/videos/{videoId}/progress")
    public ResponseEntity<ApiResponse<Integer>> getUserVideoProgress(
            @PathVariable Integer userId,
            @PathVariable Integer videoId) {
        Integer progress = watchHistoryService.getUserVideoProgress(userId, videoId);
        return ResponseEntity.ok(ApiResponse.success(progress));
    }

    /**
     * 更新用户观看视频的进度
     */
    @PutMapping("/users/{userId}/videos/{videoId}/progress")
    public ResponseEntity<ApiResponse<WatchHistory>> updateWatchProgress(
            @PathVariable Integer userId,
            @PathVariable Integer videoId,
            @RequestBody WatchHistory watchHistoryDetails) {
        // 检查视频是否存在
        if (!videoService.findVideoById(videoId).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "视频不存在"));
        }

        WatchHistory updatedHistory = watchHistoryService.updateWatchProgress(
                userId, videoId, watchHistoryDetails.getProgress());
        return ResponseEntity.ok(ApiResponse.success("观看进度更新成功", updatedHistory));
    }

    /**
     * 删除观看历史
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWatchHistory(@PathVariable Integer id) {
        Optional<WatchHistory> watchHistory = watchHistoryService.findWatchHistoryById(id);
        if (!watchHistory.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "观看历史记录不存在"));
        }

        watchHistoryService.deleteWatchHistory(id);
        return ResponseEntity.ok(ApiResponse.success("观看历史已成功删除", null));
    }

    /**
     * 删除用户观看特定视频的历史
     */
    @DeleteMapping("/users/{userId}/videos/{videoId}")
    public ResponseEntity<ApiResponse<Void>> deleteUserVideoWatchHistory(
            @PathVariable Integer userId,
            @PathVariable Integer videoId) {
        if (!watchHistoryService.hasUserWatchedVideo(userId, videoId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "未找到该用户观看该视频的记录"));
        }

        watchHistoryService.deleteUserVideoWatchHistory(userId, videoId);
        return ResponseEntity.ok(ApiResponse.success("观看历史已成功删除", null));
    }

    /**
     * 获取用户观看的不同视频数量
     */
    @GetMapping("/users/{userId}/count")
    public ResponseEntity<ApiResponse<Long>> getWatchedVideosCount(@PathVariable Integer userId) {
        long count = watchHistoryService.getWatchedVideosCount(userId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }
} 