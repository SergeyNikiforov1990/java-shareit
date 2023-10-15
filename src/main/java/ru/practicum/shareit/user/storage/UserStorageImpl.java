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
    private Map<String, User> userMap = new HashMap<>();
    private final AtomicInteger id = new AtomicInteger(0);

    @Override
    public User addUser(User user) {
        if (userMap.containsKey(user.getEmail())) {
            throw new UserAlreadyExistException(String.format("Пользователь с таким email: уже существует",
                    user.getEmail()));
        }
        int userId = id.incrementAndGet();

        user.setId(userId);
        userMap.put(user.getEmail(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User getUserById(int id) {
        return userMap.values().stream()
                .filter(u -> u.getId() == (id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь с id: %d не найден", id)));
    }

    @Override
    public User updateUser(User user, int userId) {
        User updatedUser = userMap.values().stream()
                .filter(u -> u.getId() == (userId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь с id: %d не найден", id)));

        String updatedUserEmail = updatedUser.getEmail();

        if (userMap.containsKey(user.getEmail()) && !updatedUserEmail.equals(user.getEmail())) {
            throw new UserAlreadyExistException(String.format("Пользователь с таким email: уже существует",
                    user.getEmail()));
        }
        if (user.getName() != null && !user.getName().isBlank()) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            updatedUser.setEmail(user.getEmail());
        }
        userMap.remove(updatedUserEmail);
        userMap.put(updatedUser.getEmail(), updatedUser);
        return updatedUser;
    }

    @Override
    public void deleteUser(int id) {
        User removedUser = userMap.values().stream()
                .filter(u -> u.getId() == (id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь с id: %d not found", id)));
        userMap.remove(removedUser.getEmail());
    }

}
