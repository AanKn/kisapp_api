package com.example.kidappapi.repository;

import com.example.kidappapi.model.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 视频数据访问层
 */
@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    
    /**
     * 根据视频类型查找视频
     */
    Page<Video> findByType(Video.VideoType type, Pageable pageable);
    
    /**
     * 根据标题模糊搜索视频
     */
    Page<Video> findByTitleContaining(String title, Pageable pageable);
    
    /**
     * 根据视频类型和标题模糊搜索视频
     */
    Page<Video> findByTypeAndTitleContaining(Video.VideoType type, String title, Pageable pageable);
    
    /**
     * 查找热门视频（按点赞数排序）
     */
    @Query("SELECT v FROM Video v ORDER BY v.likesCount DESC")
    Page<Video> findHotVideos(Pageable pageable);
    
    /**
     * 查找最新视频（按创建时间排序）
     */
    @Query("SELECT v FROM Video v ORDER BY v.createdAt DESC")
    Page<Video> findLatestVideos(Pageable pageable);
    
    /**
     * 根据视频类型查找热门视频
     */
    @Query("SELECT v FROM Video v WHERE v.type = ?1 ORDER BY v.likesCount DESC")
    Page<Video> findHotVideosByType(Video.VideoType type, Pageable pageable);
    
    /**
     * 根据视频类型查找最新视频
     */
    @Query("SELECT v FROM Video v WHERE v.type = ?1 ORDER BY v.createdAt DESC")
    Page<Video> findLatestVideosByType(Video.VideoType type, Pageable pageable);
} 