package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, int sharerUserId);

    ItemDto updateItem(int idUser, int itemId, ItemDto itemDto);

    ItemDto getItemById(int itemId, int userId);

    List<ItemDto> getAllItemsByOwnerId(int id);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(int userId, int itemId, CommentDto commentDto);

}
