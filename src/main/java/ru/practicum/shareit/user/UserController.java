package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.validation.Create;
import ru.practicum.shareit.utils.validation.Update;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        log.info("Запрос на создание пользователя");
        return userService.addUser(userDto);
    }


    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") int userId) {
        log.info("Запрос на поиск пользователя по id: " + userId);
        return userService.getUserById(userId);
    }


    @PatchMapping("/{id}")
    public UserDto update(@PathVariable("id") int userId,
                          @Validated(Update.class) @RequestBody UserDto userDto) {
        log.info("Запрос на обновление пользователя с id" + userId);
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int userId) {
        log.info("Запрос на удаление пользователя с id" + userId);
        userService.deleteUserById(userId);
    }
}
