package com.example.bankcards.service;

import com.example.bankcards.entity.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
    User getUserByUsername(String username);
    User createUser(User user);
}