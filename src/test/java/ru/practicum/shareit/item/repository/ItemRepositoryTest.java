package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .id(1)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build());

        item = itemRepository.save(Item.builder()
                .id(1)
                .name("Щётка для обуви")
                .description("Стандартная щётка для обуви")
                .available(true)
                .owner(user)
                .build());
    }

    @AfterEach
    void tearDown() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void searchItemTest() {
        Pageable pageable = PageRequest.of(0, 2);
        String searchTerm = "Щётка".toLowerCase();
        Page<Item> itemPage = itemRepository.search(searchTerm, pageable);

        assertTrue(itemPage.hasContent());

        List<Item> itemList = itemPage.getContent();
        assertEquals(1, itemList.size());

        Item foundItem = itemList.get(0);
        assertEquals(item.getId(), foundItem.getId());
        assertEquals("Щётка для обуви", foundItem.getName());
        assertEquals("Стандартная щётка для обуви", foundItem.getDescription());
    }
}
