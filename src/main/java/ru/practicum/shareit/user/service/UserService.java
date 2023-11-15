package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto dto); // отличается

    UserDto updateUser(int userId, UserDto dto); // отличается

    UserDto getUserById(int id);

    List<UserDto> getAllUsers();

    void deleteUserById(int id);
}