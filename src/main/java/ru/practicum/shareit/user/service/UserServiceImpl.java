package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto dto) {
        User user = UserMapper.toUser(dto); // создаем юзера из поступившего DTO
        return UserMapper.toUserDto(userRepository.save(user)); // сохраняем юзера, переводим его снова в DTO и возвращаем
    }

    @Override
    public UserDto getUserById(int id) {
        if (!isUserExist(id)) {
            throw new EntityNotFoundException("User не существует");
        }
        User user = userRepository.findById(id).get();
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(int id, UserDto userDto) {
        userDto.setId(id);
        User newuser = UserMapper.toUser(userDto);
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("Пользователя с id " + id + " не существует!");
        }

        User user = userOptional.get();

        if (newuser.getEmail() != null && !user.getEmail().isBlank()) {
            user.setEmail(newuser.getEmail());
        }
        if (newuser.getName() != null && !user.getName().isBlank()) {
            user.setName(newuser.getName());
        }
        return UserMapper.toUserDto(userRepository.save(user));

    }

    @Override
    public void deleteUserById(int id) {
        if (!isUserExist(id)) {
            throw new EntityNotFoundException("Пользователя не существует!");
        }
        userRepository.deleteById(id);
    }

    public boolean isUserExist(int userId) {
        var userOptional = userRepository.findById(userId);
        return !userOptional.isEmpty();
    }

}
