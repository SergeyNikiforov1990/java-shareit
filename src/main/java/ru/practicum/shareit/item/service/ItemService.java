package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, int sharerUserId);

    ItemDto updateItem(int itemId, ItemDto itemDto, int sharerUserId);

    ItemDto getItemById(int id);

    List<ItemDto> getAllItemsByOwnerId(int id);

    List<ItemDto> searchItems(String text);

    void removeItem(int id);
}
