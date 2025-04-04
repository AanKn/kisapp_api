package com.example.kidappapi.controller;

import com.example.kidappapi.model.dto.ApiResponse;
import com.example.kidappapi.model.entity.Video;
import com.example.kidappapi.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 视频控制器，提供RESTful API接口
 */
@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    /**
     * 创建视频
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Video>> createVideo(@RequestBody Video video) {
        Video savedVideo = videoService.saveVideo(video);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("视频创建成功", savedVideo));
    }

    /**
     * 获取所有视频（分页）
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Video>>> getAllVideos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Video> videos = videoService.findAllVideos(PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success(videos));
    }

    /**
     * 根据ID获取视频
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Video>> getVideoById(@PathVariable Integer id) {
        Optional<Video> video = videoService.findVideoById(id);
        if (video.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(video.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "视频不存在"));
        }
    }

    /**
     * 根据类型获取视频
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<Page<Video>>> getVideosByType(
            @PathVariable Video.VideoType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Video> videos = videoService.findVideosByType(type, PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success(videos));
    }

    /**
     * 搜索视频
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Video>>> searchVideos(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Video.VideoType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Video> videos;
        if (title != null && type != null) {
            videos = videoService.searchVideosByTypeAndTitle(type, title, PageRequest.of(page, size));
        } else if (title != null) {
            videos = videoService.searchVideosByTitle(title, PageRequest.of(page, size));
        } else if (type != null) {
            videos = videoService.findVideosByType(type, PageRequest.of(page, size));
        } else {
            videos = videoService.findAllVideos(PageRequest.of(page, size));
        }
        return ResponseEntity.ok(ApiResponse.success(videos));
    }

    /**
     * 获取热门视频
     */
    @GetMapping("/hot")
    public ResponseEntity<ApiResponse<Page<Video>>> getHotVideos(
            @RequestParam(required = false) Video.VideoType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Video> videos;
        if (type != null) {
            videos = videoService.findHotVideosByType(type, PageRequest.of(page, size));
        } else {
            videos = videoService.findHotVideos(PageRequest.of(page, size));
        }
        return ResponseEntity.ok(ApiResponse.success(videos));
    }

    /**
     * 获取最新视频
     */
    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<Page<Video>>> getLatestVideos(
            @RequestParam(required = false) Video.VideoType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Video> videos;
        if (type != null) {
            videos = videoService.findLatestVideosByType(type, PageRequest.of(page, size));
        } else {
            videos = videoService.findLatestVideos(PageRequest.of(page, size));
        }
        return ResponseEntity.ok(ApiResponse.success(videos));
    }

    /**
     * 更新视频
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Video>> updateVideo(
            @PathVariable Integer id,
            @RequestBody Video videoDetails) {
        Optional<Video> videoOptional = videoService.findVideoById(id);
        if (!videoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "视频不存在"));
        }

        Video video = videoOptional.get();
        video.setTitle(videoDetails.getTitle());
        video.setDescription(videoDetails.getDescription());
        video.setUrl(videoDetails.getUrl());
        video.setThumbnailUrl(videoDetails.getThumbnailUrl());
        video.setDuration(videoDetails.getDuration());
        video.setType(videoDetails.getType());

        Video updatedVideo = videoService.updateVideo(video);
        return ResponseEntity.ok(ApiResponse.success("视频更新成功", updatedVideo));
    }

    /**
     * 删除视频
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVideo(@PathVariable Integer id) {
        Optional<Video> video = videoService.findVideoById(id);
        if (!video.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "视频不存在"));
        }

        videoService.deleteVideo(id);
        return ResponseEntity.ok(ApiResponse.success("视频已成功删除", null));
    }

    /**
     * 增加视频点赞数
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<Void>> incrementLikesCount(@PathVariable Integer id) {
        Optional<Video> video = videoService.findVideoById(id);
        if (!video.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "视频不存在"));
        }

        videoService.incrementLikesCount(id);
        return ResponseEntity.ok(ApiResponse.success("点赞成功", null));
    }

    /**
     * 减少视频点赞数
     */
    @DeleteMapping("/{id}/like")
    public ResponseEntity<ApiResponse<Void>> decrementLikesCount(@PathVariable Integer id) {
        Optional<Video> video = videoService.findVideoById(id);
        if (!video.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "视频不存在"));
        }

        videoService.decrementLikesCount(id);
        return ResponseEntity.ok(ApiResponse.success("取消点赞成功", null));
    }
} 