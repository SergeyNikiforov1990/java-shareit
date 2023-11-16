package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.constants.HeaderConstants.USER_ID_HEADER;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemService itemService;
    private ItemDto itemDto;

    @BeforeEach
    @Test
    void setItemDto() {
        itemDto = ItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();
    }

    @SneakyThrows
    @Test
    void addItem() {
        var userId = 0;
        when(itemService.addItem(itemDto, userId)).thenReturn(itemDto);
        String contentAsString = mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, userId)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(itemDto), contentAsString);
        verify(itemService).addItem(itemDto, userId);
    }

    @SneakyThrows
    @Test
    void updateItem() {
        var itemId = 0;
        var userId = 0;
        when(itemService.updateItem(userId, itemId, itemDto)).thenReturn(itemDto);
        String contentAsString = mockMvc.perform(patch("/items/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, userId)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(itemDto), contentAsString);
        verify(itemService, times(1)).updateItem(userId, itemId, itemDto);
    }

    @SneakyThrows
    @Test
    void getItemById() {
        var itemId = 0;
        var userId = 0;
        when(itemService.getItemById(itemId, userId)).thenReturn(itemDto);
        String contentAsString = mockMvc.perform(get("/items/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, userId)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), contentAsString);
        verify(itemService, atLeast(1)).getItemById(itemId, userId);
    }

    @SneakyThrows
    @Test
    void addComment() {
        var userId = 1;
        var itemId = itemDto.getId();
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .text("text")
                .authorName("authorName")
                .created(LocalDateTime.now())
                .build();
        when(itemService.addComment(anyInt(), anyInt(), any(CommentDto.class))).thenReturn(commentDto);

        String contentAsString = mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header(USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(commentDto), contentAsString);

        verify(itemService, times(1)).addComment(userId, itemId, commentDto);
        verifyNoMoreInteractions(itemService);
    }
}
