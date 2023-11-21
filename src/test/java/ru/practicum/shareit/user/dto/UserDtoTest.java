package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserDtoTest {
    @Test
    public void createUserDtoTest() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("Sega")
                .email("sega@gmail.com")
                .build();

        assertNotNull(userDto);
        assertEquals(1, userDto.getId());
        assertEquals("Sega", userDto.getName());
        assertEquals("sega@gmail.com", userDto.getEmail());
    }

    @Test
    public void testEquals() {
        UserDto user1 = UserDto.builder()
                .id(1)
                .name("John")
                .email("john@example.com")
                .build();

        UserDto user2 = UserDto.builder()
                .id(1)
                .name("John")
                .email("john@example.com")
                .build();

        UserDto user3 = UserDto.builder()
                .id(2)
                .name("Jane")
                .email("jane@example.com")
                .build();

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
    }

    @Test
    public void testHashCode() {
        UserDto user1 = UserDto.builder()
                .id(1)
                .name("John")
                .email("john@example.com")
                .build();

        UserDto user2 = UserDto.builder()
                .id(1)
                .name("John")
                .email("john@example.com")
                .build();

        UserDto user3 = UserDto.builder()
                .id(2)
                .name("Jane")
                .email("jane@example.com")
                .build();

        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }
}
