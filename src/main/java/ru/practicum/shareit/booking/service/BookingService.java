package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(int userId, BookingDto bookingDto);
    BookingDto approved (int userId, int bookingId, boolean available);
    BookingDto getBooking (int userId, int bookingId);
    List<BookingDto> getItemsBookingsOfUser (int userId, State state);
    List<BookingDto> getBookingByItemOwner(int userId, State state);
}
