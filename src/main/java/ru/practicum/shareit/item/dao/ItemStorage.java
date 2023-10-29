package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemStorage {
    Item addItem(Item item);

    Item updateItem(int id, Item item);

    Item getItemById(int id);

    List<Item> getAllItemsByOwnerId(User user);

    List<Item> searchItem(String keyWord);

    Item findItemById(int id);

    void removeItem(int id);
}
