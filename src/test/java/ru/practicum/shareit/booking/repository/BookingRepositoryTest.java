package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private TestEntityManager entityManager;

    private User booker1;
    private User booker2;
    private Item item1;
    private Item item2;
    private Booking booking1;
    private Booking booking2;
    private Booking booking3;
    private Booking booking4;

    @BeforeEach
    void setUp() {
        booker1 = userRepository.save(User.builder()
                .id(1)
                .name("Sega")
                .email("Sega@gmail.com")
                .build());

        booker2 = userRepository.save(User.builder()
                .id(2)
                .name("Name")
                .email("Name@mail.ru")
                .build());

        item1 = itemRepository.save(Item.builder()
                .id(1)
                .name("Робот-пылесос")
                .description("Лучший работник на планете")
                .owner(booker1)
                .available(true)
                .build());

        item2 = itemRepository.save(Item.builder()
                .id(2)
                .name("Пауэрбанк")
                .description("Зарядное устройство для телефона")
                .owner(booker2)
                .available(true)
                .build());

        booking1 = bookingRepository.save(Booking.builder()
                .id(1)
                .starts(LocalDateTime.now().minusMonths(1))
                .ends(LocalDateTime.now().plusYears(1))
                .booker(booker1)
                .item(item1)
                .status(Status.REJECTED)
                .build());

        booking2 = bookingRepository.save(Booking.builder()
                .id(2)
                .starts(LocalDateTime.now().plusYears(1))
                .ends(LocalDateTime.now().plusYears(2))
                .booker(booker2)
                .item(item2)
                .status(Status.REJECTED)
                .build());

        booking3 = bookingRepository.save(Booking.builder()
                .id(3)
                .starts(LocalDateTime.now().minusMonths(1))
                .ends(LocalDateTime.now().plusYears(1))
                .booker(booker2)
                .item(item2)
                .status(Status.WAITING)
                .build());

        booking4 = bookingRepository.save(Booking.builder()
                .id(4)
                .starts(LocalDateTime.now().minusYears(1))
                .ends(LocalDateTime.now().plusDays(1))
                .booker(booker2)
                .item(item2)
                .status(Status.REJECTED)
                .build());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    public void contextLoads() {
        assertNotNull(entityManager);
    }

    @Test
    void getAllByItemOwnerIdAndStatusOrderByStartDescTest() {
        User booker = userRepository.findById(booker1.getId()).orElseThrow();
        Page<Booking> bookingList = bookingRepository.findByItemOwnerIdAndStatusOrderByStartsDesc(
                booker.getId(), Status.REJECTED, PageRequest.of(0, 1));

        assertTrue(bookingList.hasContent());

        Booking foundBooking = bookingList.getContent().get(0);

        assertEquals(booking1.getId(), foundBooking.getId());
        assertEquals("Sega", foundBooking.getBooker().getName());
        assertEquals(booking1.getStarts(), foundBooking.getStarts());
        assertEquals(booking1.getEnds(), foundBooking.getEnds());
    }

    @Test
    void getAllByBookerIdAndStatusOrderByStartDescTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> bookingPage = bookingRepository.findByBooker_IdAndStatusOrderByStartsDesc(booker2.getId(), Status.WAITING, pageable);

        List<Booking> bookingList = bookingPage.getContent();

        assertEquals(bookingList.get(0).getId(), booking3.getId());
        assertEquals(bookingList.get(0).getBooker().getName(), "Name");
        assertEquals(bookingList.get(0).getStarts(), booking3.getStarts());
        assertEquals(bookingList.get(0).getEnds(), booking3.getEnds());
    }

    @Test
    void getBookingListByBookerIdTest() {
        Pageable pageable = PageRequest.of(0, 10);
        User booker = userRepository.findById(booker1.getId()).get();
        int bookerId = booker.getId();
        Page<Booking> bookingPage = bookingRepository.findBookingsByBooker_IdOrderByStartsDesc(bookerId, pageable);

        List<Booking> bookingList = bookingPage.getContent();

        assertEquals(bookingList.get(0).getId(), booking1.getId());
    }

    @Test
    void getBookingListByOwnerIdTest() {
        Pageable pageable = PageRequest.of(0, 10);
        User owner = userRepository.findById(booker2.getId()).get();
        int ownerId = owner.getId();
        Page<Booking> bookingPage = bookingRepository.findByItemOwnerIdOrderByStartsDesc(ownerId, pageable);

        List<Booking> bookingList = bookingPage.getContent();

        assertEquals(bookingList.get(0).getId(), booking2.getId());
    }

    @Test
    void getAllByItemOwnerIdAndStatusOrderByStartsDescTest() {
        User booker = userRepository.findById(booker1.getId()).orElseThrow();
        Page<Booking> bookingList = bookingRepository.findByItemOwnerIdAndStatusOrderByStartsDesc(
                booker.getId(), Status.REJECTED, PageRequest.of(0, 1));

        assertTrue(bookingList.hasContent());

        Booking foundBooking = bookingList.getContent().get(0);

        assertEquals(booking1.getId(), foundBooking.getId());
        assertEquals("Sega", foundBooking.getBooker().getName());
        assertEquals(booking1.getStarts(), foundBooking.getStarts());
        assertEquals(booking1.getEnds(), foundBooking.getEnds());
    }

    @Test
    void getFutureBookingsByBookerTest() {
        User booker = userRepository.findById(booker2.getId()).orElseThrow();
        int bookerId = booker.getId();
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);

        Page<Booking> bookingPage = bookingRepository.findByBooker_IdAndStartsIsAfterOrderByStartsDesc(
                bookerId, localDateTime, PageRequest.of(0, Integer.MAX_VALUE));

        List<Booking> bookingList = bookingPage.getContent();

        assertEquals(bookingList.size(), 1);
        Booking foundBooking = bookingList.get(0);

        assertEquals(foundBooking.getId(), booking2.getId());
        assertEquals(foundBooking.getBooker().getName(), "Name");
        assertEquals(foundBooking.getStarts(), booking2.getStarts());
        assertEquals(foundBooking.getEnds(), booking2.getEnds());
    }
}
