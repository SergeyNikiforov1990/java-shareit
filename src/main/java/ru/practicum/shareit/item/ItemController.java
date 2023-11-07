package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.utils.validation.Create;
import ru.practicum.shareit.utils.validation.Update;

import java.util.List;

import static ru.practicum.shareit.constants.HeaderConstants.USER_ID_HEADER;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestHeader(name = USER_ID_HEADER) int userId,
                          @Validated(Create.class) @RequestBody ItemDto itemDto) {
        log.info("Запрос на добаление Item от пользователя{}", userId);
        return itemService.addItem(itemDto, userId);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(name = USER_ID_HEADER) int userId) {
        log.info("Запрос на получение Item с userId {}", userId);
        return itemService.getAllItemsByOwnerId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader(name = USER_ID_HEADER) int userId,
                           @PathVariable("itemId") int itemId) {
        log.info("Запрос на поиск Item по id={}", itemId);
        return itemService.getItemById(itemId, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader(name = USER_ID_HEADER) int userId,
                          @PathVariable("id") int itemId,
                          @Validated(Update.class) @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text) {
        log.info("Запрос на поиск item по названию={}", text);
        return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                 @RequestBody CommentDto commentDto,
                                 @PathVariable int itemId) {
        return itemService.addComment(userId, itemId, commentDto);
    }
}
