package com.example.kidappapi.service.impl;

import com.example.kidappapi.model.entity.LikedVideo;
import com.example.kidappapi.repository.LikedVideoRepository;
import com.example.kidappapi.service.LikedVideoService;
import com.example.kidappapi.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikedVideoServiceImpl implements LikedVideoService {

    private final LikedVideoRepository likedVideoRepository;
    private final VideoService videoService;

    @Autowired
    public LikedVideoServiceImpl(LikedVideoRepository likedVideoRepository, VideoService videoService) {
        this.likedVideoRepository = likedVideoRepository;
        this.videoService = videoService;
    }

    @Override
    @Transactional
    public LikedVideo likeVideo(Integer userId, Integer videoId) {
        // 检查视频是否存在
        if (!videoService.findVideoById(videoId).isPresent()) {
            throw new RuntimeException("视频不存在");
        }

        // 检查是否已经点赞
        if (hasUserLikedVideo(userId, videoId)) {
            throw new RuntimeException("已经点赞过该视频");
        }

        // 创建点赞记录
        LikedVideo likedVideo = new LikedVideo();
        likedVideo.setUserId(userId);
        likedVideo.setVideoId(videoId);

        // 保存点赞记录
        LikedVideo savedLikedVideo = likedVideoRepository.save(likedVideo);

        // 增加视频点赞数
        videoService.incrementLikesCount(videoId);

        return savedLikedVideo;
    }

    @Override
    @Transactional
    public void unlikeVideo(Integer userId, Integer videoId) {
        // 检查是否已经点赞
        if (!hasUserLikedVideo(userId, videoId)) {
            throw new RuntimeException("未点赞该视频");
        }

        // 删除点赞记录
        likedVideoRepository.deleteByUserIdAndVideoId(userId, videoId);

        // 减少视频点赞数
        videoService.decrementLikesCount(videoId);
    }

    @Override
    public boolean hasUserLikedVideo(Integer userId, Integer videoId) {
        return likedVideoRepository.existsByUserIdAndVideoId(userId, videoId);
    }

    @Override
    public Page<LikedVideo> getUserLikedVideos(Integer userId, Pageable pageable) {
        return likedVideoRepository.findByUserId(userId, pageable);
    }
} 