package com.example.usermetadata.user.service;

import com.example.usermetadata.user.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto, String key);
    UserDto getUser(Long userId);
}
