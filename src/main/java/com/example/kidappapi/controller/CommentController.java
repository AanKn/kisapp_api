package com.example.kidappapi.controller;

import com.example.kidappapi.model.dto.ApiResponse;
import com.example.kidappapi.model.dto.CommentDTO;
import com.example.kidappapi.model.entity.Comment;
import com.example.kidappapi.service.CommentService;
import com.example.kidappapi.service.VideoService;
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
 * 评论控制器，提供RESTful API接口
 */
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final VideoService videoService;

    @Autowired
    public CommentController(CommentService commentService, VideoService videoService) {
        this.commentService = commentService;
        this.videoService = videoService;
    }

    /**
     * 创建评论
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Comment>> createComment(@RequestBody Comment comment) {
        // 检查视频是否存在
        if (!videoService.findVideoById(comment.getVideoId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "视频不存在"));
        }

        Comment savedComment = commentService.saveComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("评论创建成功", savedComment));
    }

    /**
     * 获取视频评论（包含用户信息）
     */
    @GetMapping("/videos/{videoId}")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> getVideoComments(@PathVariable Integer videoId) {
        // 检查视频是否存在
        if (!videoService.findVideoById(videoId).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "视频不存在"));
        }

        List<CommentDTO> comments = commentService.getVideoComments(videoId);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }

    /**
     * 分页获取视频评论（包含用户信息）
     */
    @GetMapping("/videos/{videoId}/page")
    public ResponseEntity<ApiResponse<Page<CommentDTO>>> getVideoCommentsPaged(
            @PathVariable Integer videoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // 检查视频是否存在
        if (!videoService.findVideoById(videoId).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "视频不存在"));
        }

        // 创建分页请求，按创建时间降序排序
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<CommentDTO> commentsPage = commentService.getVideoCommentsPaged(videoId, pageRequest);
        
        return ResponseEntity.ok(ApiResponse.success(commentsPage));
    }

    /**
     * 获取评论详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Comment>> getCommentById(@PathVariable Integer id) {
        Optional<Comment> comment = commentService.findCommentById(id);
        if (comment.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(comment.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "评论不存在"));
        }
    }

    /**
     * 更新评论
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Comment>> updateComment(
            @PathVariable Integer id,
            @RequestBody Comment commentDetails) {
        Optional<Comment> commentOptional = commentService.findCommentById(id);
        if (!commentOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "评论不存在"));
        }

        Comment comment = commentOptional.get();
        comment.setContent(commentDetails.getContent());

        Comment updatedComment = commentService.updateComment(comment);
        return ResponseEntity.ok(ApiResponse.success("评论更新成功", updatedComment));
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Integer id) {
        Optional<Comment> comment = commentService.findCommentById(id);
        if (!comment.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "评论不存在"));
        }

        commentService.deleteComment(id);
        return ResponseEntity.ok(ApiResponse.success("评论已成功删除", null));
    }

    /**
     * 获取用户的所有评论
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<List<Comment>>> getUserComments(@PathVariable Integer userId) {
        List<Comment> comments = commentService.getUserComments(userId);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }

    /**
     * 获取视频评论数量
     */
    @GetMapping("/videos/{videoId}/count")
    public ResponseEntity<ApiResponse<Long>> getVideoCommentCount(@PathVariable Integer videoId) {
        // 检查视频是否存在
        if (!videoService.findVideoById(videoId).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "视频不存在"));
        }

        long count = commentService.getVideoCommentCount(videoId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }
} 