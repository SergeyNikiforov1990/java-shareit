package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequestResponseDto {
    private int id;
    private final String description;
    private List<ItemRequestResponseDtoItem> items;
    private LocalDateTime created;
}
