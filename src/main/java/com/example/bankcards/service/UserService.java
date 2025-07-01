package com.example.bankcards.service;

import com.example.bankcards.dto.UserRequest;
import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
    User getUserByUsername(String username);
    User createUser(User user);
    UserResponse createUser(UserRequest user);

    Page<UserResponse> getAllUsers(Pageable pageable);
}