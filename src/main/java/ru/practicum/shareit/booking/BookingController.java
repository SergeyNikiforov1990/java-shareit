package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.WrongStatusException;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@AllArgsConstructor
public class BookingController {
    BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto addBookingRequest(@Valid @RequestBody BookingDto bookingDto,
                                        @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Добавлен новый запрос: {}", bookingDto);
        return bookingService.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto approvedBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                      @PathVariable int bookingId,
                                      @RequestParam(name = "approved") boolean available) {
        log.info("Отправлен запрос на изменение статуса бронирования от владельца c id: {}", userId);
        var result = bookingService.approved(userId, bookingId, available);
        return result;
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                 @PathVariable int bookingId) {

        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getBookingsOfUser(@RequestHeader("X-Sharer-User-Id") int userId,
                                              @RequestParam(defaultValue = "ALL") String state) {
        State stateEnum;
        try {
            stateEnum = State.valueOf(state);

        } catch (Exception ex) {
            throw new WrongStatusException("Unknown state: UNSUPPORTED_STATUS"); // поменять
        }
        return bookingService.getItemsBookingsOfUser(userId, stateEnum);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getBookingByItemOwner(@RequestHeader("X-Sharer-User-Id") int userId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        State stateEnum;
        try {
            stateEnum = State.valueOf(state);
        } catch (Exception ex) {
            throw new WrongStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookingService.getBookingByItemOwner(userId, stateEnum);
    }

}
