package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    UserStorage userStorage;

    @Override
    public UserDto addUser(UserDto dto) {
        User user = UserMapper.toUser(dto);
        User createdUser = userStorage.addUser(user);

        return UserMapper.toUserDto(createdUser);
    }

    @Override
    public UserDto updateUser(int id, UserDto dto) {
        User user = UserMapper.toUser(dto);
        User updatedUser = userStorage.updateUser(user, id);

        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public UserDto getUserById(int id) {
        User user = userStorage.getUserById(id);
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = userStorage.getAllUsers();

        return userList.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(int id) {
        userStorage.deleteUser(id);
    }

}