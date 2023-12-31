package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    private User owner;
    private UserDto ownerDto;
    private User requester;
    private ItemDto itemDto;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUser() {
        owner = User.builder()
                .id(2)
                .name("owner")
                .email("email2@email.com")
                .build();

        requester = User.builder()
                .id(1)
                .name("requester")
                .email("email2@email.com")
                .build();
        itemDto = ItemDto.builder()
                .id(1)
                .name("item")
                .description("description")
                .owner(owner)
                .available(true)
                .build();
        itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .description("description ItemRequestDto")
                .requester(requester)
                .created(LocalDateTime.now())
                .build();

        ownerDto = UserMapper.toUserDto(owner);
    }

    @Test
    void addItemRequest() {

        int userId = requester.getId();

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);

        when(userRepository.findById(userId)).thenReturn(Optional.of(requester));
        when(itemRequestRepository.save(itemRequest)).thenReturn(itemRequest);

        ItemRequestDto result = itemRequestService.addItemRequest(userId, itemRequestDto);

        assertNotNull(result);
        assertEquals(itemRequestDto.getId(), result.getId());

        verify(itemRequestRepository).save(any());

    }

    @Test
    void getItemsRequests() {

        int userId = requester.getId();

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        List<Item> itemList = List.of(ItemMapper.toItem(itemDto, requester));
        itemList.get(0).setRequest(itemRequest);
        List<Integer> requestIds = (List.of(itemRequest)).stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());

        when(userRepository.findById(userId)).thenReturn(Optional.of(requester));
        when(itemRequestRepository.findItemRequestsByRequesterId(userId))
                .thenReturn(List.of(itemRequest));
        when(itemRepository.findItemsByRequestIdIn(requestIds)).thenReturn(itemList);

        List<ItemRequestResponseDto> result = itemRequestService.getItemsRequests(userId);

        assertNotNull(result);
        assertEquals(result.size(), List.of(itemRequest).size());
    }

    @Test
    void getAllRequests() {
        int userId = owner.getId();
        int from = 0;
        int size = 20;
        PageRequest page = PageRequest.of(from, size);

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        List<ItemRequest> expectedRequestsDto = List.of(itemRequest);
        Item item = ItemMapper.toItem(itemDto, requester);

        List<Integer> requestIds = (new PageImpl<>(List.of(itemRequest))).stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());
        List<Item> itemList = List.of(item);
        itemList.get(0).setRequest(itemRequest);

        var result = expectedRequestsDto.stream()
                .map(ItemRequestMapper::toItemRequestResponseDto)
                .collect(Collectors.toList());
        result.get(0).setItems(List.of(ItemMapper.toItemRequestResponseDtoItem(item)));

        when(itemRequestRepository.findByOrderByCreatedDesc(page))
                .thenReturn(new PageImpl<>(List.of(itemRequest)));
        when(itemRepository.findItemsByRequestIdIn(requestIds)).thenReturn(itemList);

        List<ItemRequestResponseDto> actualRequestsDto = itemRequestService.getAllRequests(userId, from, size);

        assertEquals(result, actualRequestsDto);
        assertNotNull(actualRequestsDto);


    }

    @Test
    void getRequestById() {
        int userId = requester.getId();
        int requestId = itemRequestDto.getId();
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);

        Item item = ItemMapper.toItem(itemDto, requester);
        List<Item> itemList = List.of(item);

        ItemRequestResponseDto result = ItemRequestMapper.toItemRequestResponseDto(itemRequest);
        result.setItems(new ArrayList<>());
        for (Item item1 : itemList) {
            result.getItems().add(ItemMapper.toItemRequestResponseDtoItem(item1));
        }


        when(userRepository.findById(userId)).thenReturn(Optional.of(requester));
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.ofNullable(itemRequest));
        when(itemRepository.findItemsByRequestId(requestId)).thenReturn(itemList);

        ItemRequestResponseDto actual = itemRequestService.getRequestById(userId, requestId);

        assertEquals(actual, result);

    }
}

