package com.example.kidappapi.service.impl;

import com.example.kidappapi.model.entity.Video;
import com.example.kidappapi.repository.VideoRepository;
import com.example.kidappapi.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 视频服务实现类
 */
@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;

    @Autowired
    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    @Transactional
    public Video saveVideo(Video video) {
        return videoRepository.save(video);
    }

    @Override
    public Optional<Video> findVideoById(Integer id) {
        return videoRepository.findById(id);
    }

    @Override
    public Page<Video> findAllVideos(Pageable pageable) {
        return videoRepository.findAll(pageable);
    }

    @Override
    public Page<Video> findVideosByType(Video.VideoType type, Pageable pageable) {
        return videoRepository.findByType(type, pageable);
    }

    @Override
    public Page<Video> searchVideosByTitle(String title, Pageable pageable) {
        return videoRepository.findByTitleContaining(title, pageable);
    }

    @Override
    public Page<Video> searchVideosByTypeAndTitle(Video.VideoType type, String title, Pageable pageable) {
        return videoRepository.findByTypeAndTitleContaining(type, title, pageable);
    }

    @Override
    public Page<Video> findHotVideos(Pageable pageable) {
        return videoRepository.findHotVideos(pageable);
    }

    @Override
    public Page<Video> findLatestVideos(Pageable pageable) {
        return videoRepository.findLatestVideos(pageable);
    }

    @Override
    public Page<Video> findHotVideosByType(Video.VideoType type, Pageable pageable) {
        return videoRepository.findHotVideosByType(type, pageable);
    }

    @Override
    public Page<Video> findLatestVideosByType(Video.VideoType type, Pageable pageable) {
        return videoRepository.findLatestVideosByType(type, pageable);
    }

    @Override
    @Transactional
    public Video updateVideo(Video video) {
        return videoRepository.save(video);
    }

    @Override
    @Transactional
    public void deleteVideo(Integer id) {
        videoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void incrementLikesCount(Integer videoId) {
        Optional<Video> videoOptional = videoRepository.findById(videoId);
        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();
            video.setLikesCount(video.getLikesCount() + 1);
            videoRepository.save(video);
        }
    }

    @Override
    @Transactional
    public void decrementLikesCount(Integer videoId) {
        Optional<Video> videoOptional = videoRepository.findById(videoId);
        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();
            if (video.getLikesCount() > 0) {
                video.setLikesCount(video.getLikesCount() - 1);
                videoRepository.save(video);
            }
        }
    }

    @Override
    @Transactional
    public void incrementCommentsCount(Integer videoId) {
        Optional<Video> videoOptional = videoRepository.findById(videoId);
        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();
            video.setCommentsCount(video.getCommentsCount() + 1);
            videoRepository.save(video);
        }
    }

    @Override
    @Transactional
    public void decrementCommentsCount(Integer videoId) {
        Optional<Video> videoOptional = videoRepository.findById(videoId);
        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();
            if (video.getCommentsCount() > 0) {
                video.setCommentsCount(video.getCommentsCount() - 1);
                videoRepository.save(video);
            }
        }
    }
} 