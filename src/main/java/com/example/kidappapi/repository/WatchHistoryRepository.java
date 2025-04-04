package com.example.kidappapi.repository;

import com.example.kidappapi.model.entity.WatchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Integer> {
    
    // 根据用户ID查询观看历史，按观看时间降序排序
    List<WatchHistory> findByUserIdOrderByWatchedAtDesc(Integer userId);
    
    // 分页查询用户观看历史
    Page<WatchHistory> findByUserId(Integer userId, Pageable pageable);
    
    // 根据用户ID和视频ID查询观看记录
    Optional<WatchHistory> findByUserIdAndVideoId(Integer userId, Integer videoId);
    
    // 根据用户ID和视频ID删除观看记录
    void deleteByUserIdAndVideoId(Integer userId, Integer videoId);
    
    // 查询用户最近7天的观看历史
    @Query("SELECT w FROM WatchHistory w WHERE w.userId = :userId AND w.watchedAt > CURRENT_DATE - 7 ORDER BY w.watchedAt DESC")
    List<WatchHistory> findUserRecentWatchHistory(@Param("userId") Integer userId);
    
    // 查询用户的所有不同视频数量
    @Query("SELECT COUNT(DISTINCT w.videoId) FROM WatchHistory w WHERE w.userId = :userId")
    long countDistinctVideosByUserId(@Param("userId") Integer userId);
} 