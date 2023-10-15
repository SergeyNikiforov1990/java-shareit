package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto dto);

    UserDto updateUser(int userId, UserDto dto);

    UserDto getUserById(int id);

    List<UserDto> getAllUsers();

    void deleteUserById(int id);
}