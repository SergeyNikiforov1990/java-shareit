package ru.practicum.request.service;

import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestResponseDto> getItemsRequests(long userId);

    List<ItemRequestResponseDto> getAllRequests(long userId, int from, int size);

    ItemRequestResponseDto getRequestById(Long userId, long requestId);

}
