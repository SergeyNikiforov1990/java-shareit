package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User getUserById(int id);

    User addUser(User user);

    User updateUser(User user, int userId);

    List<User> getAllUsers();

    void deleteUser(int id);
}