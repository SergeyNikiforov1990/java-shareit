package ru.practicum.shareit.request.Dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ItemRequestResponseDtoTest {
    private ItemRequestResponseDto requestDto;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testSetAndGetId() {
        requestDto = ItemRequestResponseDto.builder()
                .id(1)
                .build();
        int id = 1;
        assertEquals(id, requestDto.getId());
    }

    @Test
    public void testSetAndGetDescription() {
        requestDto = ItemRequestResponseDto.builder()
                .description("This is a test description")
                .build();
        String description = "This is a test description";
        assertEquals(description, requestDto.getDescription());
    }


    @Test
    public void testSetAndGetCreatedTime() {
        LocalDateTime created = LocalDateTime.now();
        requestDto = ItemRequestResponseDto.builder()
                .created(created)
                .build();
        assertEquals(created, requestDto.getCreated());
    }

}
