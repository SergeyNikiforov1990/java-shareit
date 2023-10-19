package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UserAlreadyExistException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@RequiredArgsConstructor
public class UserStorageImpl implements UserStorage {
    private final Map<Integer, User> map = new HashMap<>();
    private final AtomicInteger id = new AtomicInteger(0);

    @Override
    public User addUser(User user) {
        if (map.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new UserAlreadyExistException(String.format("Пользователь с таким email %s уже существует",
                    user.getEmail()));
        }
        int userId = id.incrementAndGet();
        user.setId(userId);
        map.put(userId, user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(map.values());
    }

    @Override
    public User getUserById(int id) {
        if (!map.containsKey(id)) {
            throw new EntityNotFoundException(String.format("Пользователь с id: %d не найден", id));
        }
        return map.get(id);
    }

    @Override
    public User updateUser(User user, int userId) {
        if (!map.containsKey(userId)) {
            throw new EntityNotFoundException(String.format("Пользователь с id: %d не найден", id));
        }
        User updatedUser = map.get(userId);
        boolean hasDuplicateEmail = map.values().stream()
                .anyMatch(u -> u.getId() != userId && u.getEmail().equals(user.getEmail()));

        if (hasDuplicateEmail) {
            throw new UserAlreadyExistException(String.format("Пользователь с таким email: %s уже существует",
                    user.getEmail()));
        }

        if (user.getName() != null && !user.getName().isBlank()) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            updatedUser.setEmail(user.getEmail());
        }
        map.remove(updatedUser.getId());
        map.put(userId, updatedUser);
        return updatedUser;
    }

    @Override
    public void deleteUser(int id) {
        map.remove(id);
    }
}