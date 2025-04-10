package com.example.kidappapi.controller;

import com.example.kidappapi.model.dto.ApiResponse;
import com.example.kidappapi.model.dto.UserRegistrationRequest;
import com.example.kidappapi.model.entity.User;
import com.example.kidappapi.service.UserService;
import com.example.kidappapi.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * 用户控制器，提供RESTful API接口
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final VerificationCodeService verificationCodeService;

    @Autowired
    public UserController(UserService userService, VerificationCodeService verificationCodeService) {
        this.userService = userService;
        this.verificationCodeService = verificationCodeService;
    }

    /**
     * 带邮箱验证的用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        // 检查用户名是否已存在
        if (userService.isUsernameExists(request.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "用户名已存在"));
        }
        
        // 检查邮箱是否已被注册
        if (userService.isEmailExists(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "邮箱已被注册"));
        }
        
        // 验证邮箱验证码
        boolean isCodeValid = verificationCodeService.verifyCode(request.getEmail(), request.getVerificationCode());
        if (!isCodeValid) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "验证码无效或已过期"));
        }
        
        // 创建用户实体
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname());
        user.setAvatarUrl(request.getAvatarUrl());
        user.setBackgroundUrl(request.getBackgroundUrl());
        user.setSignature(request.getSignature());
        
        // 保存用户
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("用户注册成功", savedUser));
    }

    /**
     * 创建用户 (旧版，不带邮箱验证)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
        // 检查用户名是否已存在
        if (userService.isUsernameExists(user.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "用户名已存在"));
        }

        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("用户创建成功", savedUser));
    }

    /**
     * 获取所有用户
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(user.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "用户不存在"));
        }
    }

    /**
     * 根据用户名查找用户
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<User>> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findUserByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(user.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "用户不存在"));
        }
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> optionalUser = userService.findUserById(id);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "用户不存在"));
        }

        User user = optionalUser.get();
        
        // 检查用户名是否已被其他用户使用
        if (!user.getUsername().equals(userDetails.getUsername()) && 
                userService.isUsernameExists(userDetails.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "用户名已存在"));
        }
        
        // 更新用户信息
        user.setUsername(userDetails.getUsername());
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(userDetails.getPassword());
        }
        user.setNickname(userDetails.getNickname());
        user.setSignature(userDetails.getSignature());
        user.setAvatarUrl(userDetails.getAvatarUrl());
        user.setBackgroundUrl(userDetails.getBackgroundUrl());
        user.setEmail(userDetails.getEmail());
        
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(ApiResponse.success("用户更新成功", updatedUser));
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "用户不存在"));
        }
        
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("用户已成功删除", null));
    }
} 