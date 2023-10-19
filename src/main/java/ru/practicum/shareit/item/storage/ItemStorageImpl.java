package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ItemStorageImpl implements ItemStorage {
    private final Map<Integer, Item> itemMap = new HashMap<>();
    private final AtomicInteger generatedItemId = new AtomicInteger(0);
    private final Map<Integer, List<Item>> userItemIndex = new LinkedHashMap<>();

    @Override
    public Item addItem(Item item) {
        int id = generatedItemId.incrementAndGet();
        item.setId(id);
        itemMap.put(id, item);
        final List<Item> items = userItemIndex.computeIfAbsent(item.getOwner().getId(), k -> new ArrayList<>());
        items.add(item);
        return item;
    }

    @Override
    public Item updateItem(int id, Item item) {
        Item updatedItem = findItemById(id);

        if (!updatedItem.getOwner().equals(item.getOwner())) {
            log.warn("Id = {} для этого элемента не найден", item.getId());
            throw new EntityNotFoundException("Id не найден для этого item!");
        }

        if (item.getName() != null && !item.getName().isBlank()) {
            updatedItem.setName(item.getName());
        }

        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            updatedItem.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }

        return updatedItem;
    }

    @Override
    public Item getItemById(int id) {
        return itemMap.get(id);
    }

    @Override
    public List<Item> getAllItemsByOwnerId(User user) {
        return userItemIndex.getOrDefault(user.getId(), new ArrayList<>());
    }

    @Override
    public void removeItem(int id) {
        Item removedItem = itemMap.remove(id);
        if (removedItem != null) {
            List<Item> userItems = userItemIndex.get(removedItem.getOwner().getId());
            userItems.remove(removedItem);
        }
    }

    @Override
    public List<Item> searchItem(String text) {
        List<Item> searchResultList = new ArrayList<>();
        if (text.isBlank()) {
            return searchResultList;
        }

        return itemMap.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text) ||
                        item.getDescription().toLowerCase().contains(text) &&
                                item.getAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public Item findItemById(int id) {
        return Optional.ofNullable(itemMap.get(id))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь с id: %d не найден", id)));
    }
}
