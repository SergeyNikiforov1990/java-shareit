package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ItemDto addItem(ItemDto itemDto, int userId) {
        UserDto userDto = userService.getUserById(userId);
        Item item = ItemMapper.toItem(itemDto, UserMapper.toUser(userDto));
        return ItemMapper.toItemDto(itemRepository.save(item));

    }

    @Override
    public ItemDto updateItem(int idUser, int itemId, ItemDto itemDto) {
        Optional<User> user1 = userRepository.findById(idUser);
        User user;
        if (user1.isPresent()) {
            user = user1.get();
        } else {
            // обработка случая, когда объект Item не найден
            throw new EntityNotFoundException("User не найден");
        }

        Optional<Item> item1 = itemRepository.findById(itemId);
        Item item;
        if (item1.isPresent()) {
            item = item1.get();
        } else {
            // обработка случая, когда объект Item не найден
            throw new EntityNotFoundException("Вещь не найдена для заданного идентификатора");
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItemById(int itemId, int userId) {
        var item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException("Вещь не найдена!"));
        List<Comment> comments = commentRepository.findCommentsByItem_Id(itemId);
        var itemDto = ItemMapper.toItemDto(item);
        List<CommentDto> commentsDto = new ArrayList<>();
        for (Comment comment : comments) {
            commentsDto.add(CommentMapper.tocommentDto(comment));
        }
        itemDto.setComments(commentsDto);

        if (userId == item.getOwner().getId()) {
            var bookings = bookingRepository.findBookingByItem_IdAndStatus(item.getId(), Status.APPROVED);

            if (bookings.size() != 0) {
                bookings = bookings.stream()
                        .sorted(Comparator.comparing(Booking::getStarts).reversed())
                        .collect(Collectors.toList());

                for (Booking booking : bookings) {
                    if (booking.getStarts().isBefore(LocalDateTime.now())) {
                        itemDto.setLastBooking(BookingMapper.toBookingDto(booking));
                        break;
                    }
                }

                bookings = bookings.stream()
                        .sorted(Comparator.comparing(Booking::getStarts))
                        .collect(Collectors.toList());

                for (Booking booking : bookings) {
                    if (booking.getStarts().isAfter(LocalDateTime.now())) {
                        itemDto.setNextBooking(BookingMapper.toBookingDto(booking));
                        break;
                    }
                }
            }
        }
        return itemDto;
    }

    @Override
    public List<ItemDto> getAllItemsByOwnerId(int userId) {
        List<ItemDto> itemDtos = new ArrayList<>();
        List<Item> items = itemRepository.findItemByOwnerId(userId);

        items = items.stream()
                .sorted(Comparator.comparingLong(Item::getId))
                .collect(Collectors.toList());

        for (Item item : items) {
            var itemDto = ItemMapper.toItemDto(item);

            if (userId == item.getOwner().getId()) {
                var bookings = bookingRepository.findBookingByItem_IdAndStatus(item.getId(), Status.APPROVED);

                if (bookings.size() > 0) {
                    bookings = bookings.stream()
                            .sorted(Comparator.comparing(Booking::getStarts).reversed())
                            .collect(Collectors.toList());

                    for (Booking booking : bookings) {
                        if (booking.getStarts().isBefore(LocalDateTime.now())) {
                            itemDto.setLastBooking(BookingMapper.toBookingDto(booking));
                            break;
                        }
                    }
                    bookings = bookings.stream()
                            .sorted(Comparator.comparing(Booking::getStarts))
                            .collect(Collectors.toList());
                    for (Booking booking : bookings) {
                        if (booking.getStarts().isAfter(LocalDateTime.now())) {
                            itemDto.setNextBooking(BookingMapper.toBookingDto(booking));
                            break;
                        }
                    }
                }
            }
            List<Comment> comments = commentRepository.findCommentsByItem_Id(item.getId());
            List<CommentDto> commentsDto = new ArrayList<>();
            for (Comment comment : comments) {
                commentsDto.add(CommentMapper.tocommentDto(comment));
            }
            itemDto.setComments(commentsDto);

            itemDtos.add(itemDto);
        }
        return itemDtos;
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        List<ItemDto> itemDtos = new ArrayList<>();
        if (text.isBlank()) {
            return itemDtos;
        }
        List<Item> items = itemRepository.search(text);
        for (Item item : items) {
            if (item.getAvailable()) {
                itemDtos.add(ItemMapper.toItemDto(item));
            }
        }

        return itemDtos;
    }

    @Override
    public CommentDto addComment(int userId, int itemId, CommentDto commentDto) {
        var itemOptional = itemRepository.findById(itemId);

        if (itemOptional.isEmpty()) {
            throw new EntityNotFoundException("Такой вещи нет");
        }
        var item = itemOptional.get();

        var userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("Такого пользователя нет");
        }
        var user = userOptional.get();

        List<Booking> bookings = bookingRepository.findBookingByItem_Id(itemId);
        boolean isExist = false;
        for (Booking booking : bookings) {
            if (booking.getBooker().getId() == userId
                    && booking.getStarts().isBefore(LocalDateTime.now())
                    && booking.getStatus().equals(Status.APPROVED)) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            throw new ValidationException("Этой вещью не пользовался данный пользователь!");
        }

        Comment comment = CommentMapper.toComment(commentDto);
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        var result = CommentMapper.tocommentDto(commentRepository.save(comment));
        return result;
    }
}
