package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    private User expectedUser;

    @BeforeEach
    void setUser() {
        expectedUser = User.builder()
                .id(1)
                .name("Sergey")
                .email("nikiforovsn@mail.ru")
                .build();
    }

    @Test
    void addUser_whenUserNameValid_thenSaveUser() {
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        UserDto actualUser = userService.addUser(UserMapper.toUserDto(expectedUser));

        assertEquals(UserMapper.toUser(actualUser), expectedUser);
        verify(userRepository).save(expectedUser);
    }

    @Test
    void updateUserWhenUserFound() {
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(expectedUser));

        var returnedUser = userService.updateUser(expectedUser.getId(), UserMapper.toUserDto(expectedUser));

        assertThat(UserMapper.toUser(returnedUser), equalTo(expectedUser));
        verify(userRepository).save(expectedUser);
        verify(userRepository).findById(expectedUser.getId());
    }

    @Test
    void update_whenAddUserWithIncorrectId_thenThrowObjectNotFoundException() {
        // Меняем eq(expectedUser.getId()) на anyInt() или любое другое значение id
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                userService.getUserById(expectedUser.getId()));

        assertThat(exception.getMessage(), equalTo("User не существует"));
        verify(userRepository, never()).save(expectedUser);
        // Изменяем expectedUser.getId() на anyInt()
        verify(userRepository).findById(anyInt());
    }

    @Test
    void getAllUsers() {
        List<User> expectedUsers = List.of(expectedUser);
        List<UserDto> expectedUserDto = expectedUsers.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<UserDto> actualUsersDto = userService.getAllUsers();

        assertEquals(actualUsersDto.size(), 1);
        assertEquals(actualUsersDto, expectedUserDto);
    }

    @Test
    void deleteWhenUserFound_ThenDeletenUser() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(expectedUser));
        userService.deleteUserById(expectedUser.getId());
        verify(userRepository, times(1)).deleteById(expectedUser.getId());
    }

    @Test
    void deleteWhenUserNotFound_thenThrowObjectNotFoundException() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                userService.deleteUserById(expectedUser.getId()));

        assertThat(exception.getMessage(), equalTo("Пользователя не существует!"));
        verify(userRepository, never()).deleteById(expectedUser.getId());
    }

    @Test
    void getUserByIdWhenUserFound_ThenReturnUser() {
        when(userRepository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));
        UserDto actualUser = userService.getUserById(expectedUser.getId());
        assertEquals(UserMapper.toUser(actualUser), expectedUser);
    }

    @Test
    void getUserByIdWhenUserFound_ThenObjectNotFoundException() {
        when(userRepository.findById(expectedUser.getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                userService.getUserById(expectedUser.getId()));
    }


}
