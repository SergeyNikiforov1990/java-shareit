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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.shareit.constants.HeaderConstants.USER_ID_HEADER;


@RestController
@RequestMapping(path = "/bookings")
@Slf4j
//@RequiredArgsConstructor
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto addBookingRequest(@Valid @RequestBody BookingDto bookingDto,
                                        @RequestHeader(name = USER_ID_HEADER) int userId) {
        log.info("Добавлен новый запрос: {}", bookingDto);
        return bookingService.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto approveBooking(@RequestHeader(name = USER_ID_HEADER) int userId,
                                     @PathVariable int bookingId,
                                     @RequestParam(name = "approved") boolean available) {
        log.info("Отправлен запрос на изменение статуса бронирования от владельца c id: {}", userId);
        var result = bookingService.approve(userId, bookingId, available);
        return result;
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getBooking(@RequestHeader(name = USER_ID_HEADER) int userId,
                                 @PathVariable int bookingId) {

        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getBookingsOfUser(@RequestHeader(name = USER_ID_HEADER) int userId,
                                              @RequestParam(defaultValue = "ALL") String state,
                                              @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                              @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size) {
        State stateEnum;
        try {
            stateEnum = State.valueOf(state);
        } catch (Exception ex) {
            throw new WrongStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookingService.getItemsBookingsOfUser(userId, stateEnum, from, size);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getBookingByItemOwner(@RequestHeader(name = USER_ID_HEADER) int userId,
                                                  @RequestParam(defaultValue = "ALL") String state,
                                                  @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                  @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size) {
        State stateEnum;
        try {
            stateEnum = State.valueOf(state);
        } catch (Exception ex) {
            throw new WrongStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookingService.getBookingByItemOwner(userId, stateEnum, from, size);
    }
}
