package ru.practicum.shareit.booking.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.utils.validation.Create;
import ru.practicum.shareit.utils.validation.Update;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDto item;
    private int itemId;
    private UserDto booker;
    private int bookerId;
    private Status status;
}
