package com.example.bankcards.service;

import com.example.bankcards.dto.UserRequest;
import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    UserResponse getUserById(UUID id);
    User getUserByUsername(String username);
    UserResponse createUser(UserRequest request);

    Page<UserResponse> getAllUsers(Pageable pageable);

    void deleteUser(UUID id);
}