package com.example.kidappapi.service.impl;

import com.example.kidappapi.model.entity.User;
import com.example.kidappapi.repository.UserRepository;
import com.example.kidappapi.service.UserService;
import com.example.kidappapi.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VerificationCodeService verificationCodeService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, VerificationCodeService verificationCodeService) {
        this.userRepository = userRepository;
        this.verificationCodeService = verificationCodeService;
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> login(String account, String password) {
        // 首先尝试用账号作为用户名登录
        Optional<User> userByUsername = userRepository.findByUsername(account);
        if (userByUsername.isPresent() && userByUsername.get().getPassword().equals(password)) {
            return userByUsername;
        }
        
        // 如果用户名登录失败，尝试用账号作为邮箱登录
        Optional<User> userByEmail = userRepository.findByEmail(account);
        if (userByEmail.isPresent() && userByEmail.get().getPassword().equals(password)) {
            return userByEmail;
        }
        
        // 如果都登录失败，返回空
        return Optional.empty();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean isUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean isNicknameExists(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public boolean changePassword(String email, String verificationCode, String newPassword) {
        // 1. 验证邮箱是否存在
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            return false;
        }
        
        // 2. 验证验证码是否正确
        if (!verificationCodeService.verifyCode(email, verificationCode)) {
            return false;
        }
        
        // 3. 更新密码
        User user = userOptional.get();
        user.setPassword(newPassword);
        userRepository.save(user);
        
        // 4. 验证成功后，使验证码失效
        verificationCodeService.invalidateCode(email);
        
        return true;
    }
} 