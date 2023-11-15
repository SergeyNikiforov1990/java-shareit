package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.validation.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {
    private int id;
    @NotBlank(message = "Name cannot be empty", groups = {Create.class})
    private String name;

    @NotBlank(message = "Description cannot be empty", groups = {Create.class})
    private String description;

    @NotNull(message = "Booking status cannot be empty", groups = {Create.class})
    private Boolean available;
    private User owner;
    private ItemRequest request;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private int requestId;
    private List<CommentDto> comments = new ArrayList<>();
}
