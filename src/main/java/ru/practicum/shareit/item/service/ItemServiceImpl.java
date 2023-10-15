package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemStorage itemStorage;
    @Autowired
    UserStorage userStorage;

    @Override
    public ItemDto addItem(ItemDto itemDto, int userId) {
        User user = userStorage.getUserById(userId);
        Item item = ItemMapper.toItem(itemDto, user);
        Item createdItem = itemStorage.addItem(item);
        return ItemMapper.toItemDto(createdItem);
    }

    @Override
    public ItemDto updateItem(int itemId, ItemDto itemDto, int userId) {
        User user = userStorage.getUserById(userId);
        Item item = ItemMapper.toItem(itemDto, user);
        Item updatedItem = itemStorage.updateItem(itemId, item);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDto getItemById(int id) {
        Item item = itemStorage.findItemById(id);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getAllItemsByOwnerId(int id) {
        User user = userStorage.getUserById(id);
        List<Item> itemList = itemStorage.getAllItemsByOwnerId(user);
        return itemList.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        List<Item> searchResult = itemStorage.searchItem(text.toLowerCase(Locale.ROOT));
        return searchResult.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public void removeItem(int id) {
        itemStorage.removeItem(id);
    }
}
