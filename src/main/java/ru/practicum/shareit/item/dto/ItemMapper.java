package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestResponseDtoItem;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class ItemMapper {
    public ItemDto toItemDto(Item item) { // Item в объект ItemDto (все GET запросы)
        return ItemDto
                .builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() == null ? 0 : item.getRequest().getId())
                .build();
    }

    public Item toItem(ItemDto itemDto, User user) { // ItemDto в Item для сохранениия в БД (POST)
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(user)
                .build();
    }

    public static ItemRequestResponseDtoItem toItemRequestResponseDtoItem(Item item) { // Item -> в объект ItemDto
        return ItemRequestResponseDtoItem.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() == null ? 0 : item.getRequest().getId())
                .build();
    }
}
