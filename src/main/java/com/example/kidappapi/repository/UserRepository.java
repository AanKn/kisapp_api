package com.example.kidappapi.repository;

import com.example.kidappapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问层
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根据昵称查找用户
     */
    Optional<User> findByNickname(String nickname);
    
    /**
     * 判断用户名是否已存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 判断昵称是否已存在
     */
    boolean existsByNickname(String nickname);
} 