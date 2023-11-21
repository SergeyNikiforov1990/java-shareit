package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequestResponseDtoItem {
    private int id;
    private String name;
    private String description;
    private int requestId;
    private boolean available;
}
