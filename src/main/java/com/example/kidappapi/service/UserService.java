package com.example.kidappapi.service;

import com.example.kidappapi.model.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 保存用户
     */
    User saveUser(User user);
    
    /**
     * 根据ID查找用户
     */
    Optional<User> findUserById(Long id);
    
    /**
     * 根据用户名查找用户
     */
    Optional<User> findUserByUsername(String username);
    
    /**
     * 根据昵称查找用户
     */
    Optional<User> findUserByNickname(String nickname);
    
    /**
     * 查找所有用户
     */
    List<User> findAllUsers();
    
    /**
     * 更新用户信息
     */
    User updateUser(User user);
    
    /**
     * 删除用户
     */
    void deleteUser(Long id);
    
    /**
     * 判断用户名是否已存在
     */
    boolean isUsernameExists(String username);
    
    /**
     * 判断昵称是否已存在
     */
    boolean isNicknameExists(String nickname);
} 