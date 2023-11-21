package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.constants.HeaderConstants.USER_ID_HEADER;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingService;
    private User booker;
    private User owner;
    private ItemDto item;
    private BookingDto bookingDto;
    private int from;
    private int size;

    @BeforeEach
    @Test
    void setUpBookingDto() {
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
        item = ItemDto.builder()
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

        from = 0;
        size = 20;
    }


    @SneakyThrows
    @Test
    void addBookingRequest() {
        when(bookingService.addBooking(anyInt(), any(BookingDto.class))).thenReturn(bookingDto);

        String contentAsString = mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, booker.getId())
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(bookingDto), contentAsString);
        verify(bookingService).addBooking(booker.getId(), bookingDto);
    }


    @SneakyThrows
    @Test
    void approvedBookingRequest() {
        bookingDto.setStatus(Status.APPROVED);
        when(bookingService.approve(anyInt(), anyInt(), anyBoolean())).thenReturn(bookingDto);
        var bookingId = bookingDto.getId();
        var userId = booker.getId();


        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/{bookingId}", bookingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, booker.getId())
                        .param("approved", "true")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(bookingDto), contentAsString);
        verify(bookingService).approve(userId, bookingId, true);
    }

    @SneakyThrows
    @Test
    void getBooking() {
        var bookingId = bookingDto.getId();
        var userId = booker.getId();
        when(bookingService.getBooking(userId, bookingId)).thenReturn(bookingDto);
        String contentAsString = mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, userId)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), contentAsString);
        verify(bookingService, atLeast(1)).getBooking(bookingId, userId);

    }

    @SneakyThrows
    @Test
    void getBookingsOfUser() {
        State state = State.ALL;
        var userId = booker.getId();
        List<BookingDto> bookingDtoList = List.of(bookingDto);
        when(bookingService.getItemsBookingsOfUser(userId, state, from, size)).thenReturn(bookingDtoList);

        mockMvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, userId)
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService, times(1)).getItemsBookingsOfUser(userId, state, from, size);

    }

    @SneakyThrows
    @Test
    void getBookingByItemOwner() {
        State state = State.ALL;
        var userId = booker.getId();
        List<BookingDto> bookingDtoList = List.of(bookingDto);
        when(bookingService.getBookingByItemOwner(userId, state, from, size)).thenReturn(bookingDtoList);

        mockMvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, userId)
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService, times(1)).getBookingByItemOwner(userId, state, from, size);

    }
}
