package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/{id}")
    public ItemDto getById(@PathVariable("id") int id) {
        log.info("Запрос на поиск Item по id={}", id);
        return itemService.getItemById(id);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader(name = USER_ID_HEADER) int userId,
                          @PathVariable("id") int itemId,
                          @Validated(Update.class) @RequestBody ItemDto itemDto) {
        log.info("Запрос на обновление Item с ItemId={} с userId {}", itemId, userId);
        return itemService.updateItem(itemId, itemDto, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        log.info("Запрос на удаление Item с id={}", id);
        itemService.removeItem(id);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text) {
        log.info("Запрос на поиск item по названию={}", text);
        return itemService.searchItems(text);
    }
}
