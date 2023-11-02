package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByBooker_IdAndEndsIsBefore(int bookerId, LocalDateTime ends, Sort sort);

    List<Booking> findByBooker_IdAndStartsIsAfter(int bookerId, LocalDateTime starts, Sort sort);

    List<Booking> findByBooker_IdAndStatus(int bookerId, Status status, Sort sort);

    List<Booking> findByBooker_IdAndStartsIsBeforeAndEndsIsAfter(int bookerId, LocalDateTime starts,
                                                                 LocalDateTime ends, Sort sort);

    List<Booking> findByItemOwnerIdAndEndsIsBefore(int bookerId, LocalDateTime ends, Sort sort);

    List<Booking> findByItemOwnerIdAndStartsIsAfter(int bookerId, LocalDateTime starts, Sort sort);

    List<Booking> findByItemOwnerIdAndStatus(int bookerId, Status status, Sort sort);

    List<Booking> findByItemOwnerIdAndStartsIsBeforeAndEndsIsAfter(int bookerId, LocalDateTime starts,
                                                                   LocalDateTime ends, Sort sort);

    List<Booking> findByBooker_Id(int bookerId);

    List<Booking> findBookingByItem_Id(int itemId);

    List<Booking> findBookingByItem_IdAndStatus(int itemId, Status status);

    List<Booking> findByItemOwnerIdOrderByStartsDesc(int ownerId);
}
