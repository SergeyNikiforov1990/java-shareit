package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @SneakyThrows
    @Test
    void addUser() {
        UserDto userToCreate = new UserDto().builder().id(1)
                .name("Sergey")
                .email("nikiforovsn@mail.ru")
                .build();
        ;
        //Mockito.when(объект.имяМетода()).thenReturn(результат);
        when(userService.addUser(userToCreate)).thenReturn(userToCreate);

        String result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(userToCreate), result);
    }

    @SneakyThrows
    @Test
    void getUsers() {
        List<UserDto> userDtoList = List.of(UserDto.builder()
                .email("@yandex.ru")
                .build());
        when(userService.getAllUsers()).thenReturn(userDtoList);

        String contentAsString = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDtoList), contentAsString);

    }

    @SneakyThrows
    @Test
    void updateUser() {
        int userId = 0;
        UserDto userToUdpate = UserDto.builder()
                .name(null)
                .email("yandex@mail.ru")
                .build();

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToUdpate)))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUser(0, userToUdpate);

    }

    @SneakyThrows
    @Test
    void getUserById() {
        int userId = 0;
        mockMvc.perform(get("/users/{userId}", userId))
                .andDo(print())
                .andExpect(status().isOk());
        //Mockito.verify(объект).имяМетода(параметр);
        verify(userService).getUserById(userId);
        verify(userService, times(1)).getUserById(userId);
    }

    @SneakyThrows
    @Test
    void deleteUser() {
        int userId = 0;
        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());
        verify(userService, times(1)).deleteUserById(userId);
    }
}
