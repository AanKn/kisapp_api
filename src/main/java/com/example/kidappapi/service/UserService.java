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
     * 用户登录
     * @param account 用户名或邮箱
     * @param password 密码
     * @return 登录成功返回用户信息，失败返回空
     */
    Optional<User> login(String account, String password);
    
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
     * 根据邮箱查找用户
     */
    Optional<User> findUserByEmail(String email);
    
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
    
    /**
     * 判断邮箱是否已存在
     */
    boolean isEmailExists(String email);
    
    /**
     * 修改用户密码
     * @param email 用户邮箱
     * @param verificationCode 验证码
     * @param newPassword 新密码
     * @return 修改成功返回true，失败返回false
     */
    boolean changePassword(String email, String verificationCode, String newPassword);
} 