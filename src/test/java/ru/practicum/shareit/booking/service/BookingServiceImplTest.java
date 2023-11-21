package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;
    private User owner;
    private UserDto ownerDto;
    private User booker;
    private ItemDto itemDto;
    private BookingDto bookingDto;

    private int from;
    private int size;

    @BeforeEach
    void setUser() {
        owner = User.builder()
                .id(2)
                .name("owner")
                .email("email2@email.com")
                .build();

        booker = User.builder()
                .id(1)
                .name("booker")
                .email("email2@email.com")
                .build();
        itemDto = ItemDto.builder()
                .id(1)
                .name("item")
                .description("description")
                .owner(owner)
                .available(true)
                .build();
        bookingDto = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusWeeks(2))
                .bookerId(1)
                .itemId(1)
                .build();

        ownerDto = UserMapper.toUserDto(owner);


        from = 0;
        size = 20;
    }


    @Test
    void addBooking() {
        int userId = owner.getId();
        int itemId = bookingDto.getItemId();

        Item item = ItemMapper.toItem(itemDto, owner);
        item.setOwner(booker);

        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setStatus(Status.WAITING);
        booking.setBooker(booker);
        booking.setItem(item);

        when(userRepository.findById(userId)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.save(booking)).thenReturn(booking);

        BookingDto bookingOutDto = bookingService.addBooking(userId, bookingDto);

        assertNotNull(bookingOutDto);
        assertEquals(bookingDto.getId(), bookingOutDto.getId());

        verify(bookingRepository).save(any());

    }

    @Test
    void addBookingIsOwner_ThenReturnThrow() {
        int userId = owner.getId();
        int itemId = bookingDto.getItemId();

        Item item = ItemMapper.toItem(itemDto, owner);
        item.setOwner(owner);

        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setStatus(Status.WAITING);
        booking.setBooker(booker);
        booking.setItem(item);

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookingService.addBooking(userId, bookingDto));

        assertThat(exception.getMessage(), equalTo("Вы пытаетесь взять в аренду свою вещь"));
        verify(bookingRepository, never()).save(any());

    }

    @Test
    void approve() {
        int bookingId = bookingDto.getId();

        Item item = ItemMapper.toItem(itemDto, owner);
        item.setOwner(owner);

        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        booking.setBooker(booker);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        BookingDto result = bookingService.approve(owner.getId(), bookingId, true);

        assertThat(result.getStatus(), equalTo(Status.APPROVED));
        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository).save(any());

    }

    @Test
    void approvedWhenStatusNotApproved_ThenThrowsValidationException() {
        int bookingId = bookingDto.getId();

        Item item = ItemMapper.toItem(itemDto, owner);
        item.setOwner(owner);

        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setItem(item);
        booking.setStatus(Status.APPROVED);
        booking.setBooker(booker);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.approve(owner.getId(), bookingId, true));

        assertThat(exception.getMessage(), equalTo("Статус нельзя изменить!"));
        verify(bookingRepository, never()).save(any());

    }

    @Test
    void addBookingIsOwner_ThenReturnsThrow() {
        int userId = owner.getId();
        int itemId = bookingDto.getItemId();

        Item item = ItemMapper.toItem(itemDto, owner);
        item.setOwner(owner);

        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setStatus(Status.WAITING);
        booking.setBooker(booker);
        booking.setItem(item);

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookingService.addBooking(userId, bookingDto));

        assertThat(exception.getMessage(), equalTo("Вы пытаетесь взять в аренду свою вещь"));
        verify(bookingRepository, never()).save(any());

    }

    @Test
    void getBooking() {
        int bookingId = bookingDto.getId();

        Item item = ItemMapper.toItem(itemDto, owner);
        item.setOwner(owner);

        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setItem(item);
        booking.setBooker(booker);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        BookingDto result = bookingService.getBooking(owner.getId(), bookingId);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());

        verify(bookingRepository, atLeast(2)).findById(bookingId);

    }
}
