package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@AllArgsConstructor
@Builder
public class BookingResponseDto {
    private int id;
    private ItemDto item;
    private long itemId;
    private UserDto booker;
    private Status status;
    private LocalDateTime start;
    private LocalDateTime end;
}

