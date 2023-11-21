package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Page<Booking> findByBooker_IdAndEndsIsBeforeOrderByStartsDesc(int bookerId, LocalDateTime ends, Pageable pageable);

    Page<Booking> findByBooker_IdAndStartsIsAfterOrderByStartsDesc(int bookerId, LocalDateTime starts, Pageable pageable);

    Page<Booking> findByBooker_IdAndStatusOrderByStartsDesc(int bookerId, Status status, Pageable pageable);

    Page<Booking> findByBooker_IdAndStartsIsBeforeAndEndsIsAfterOrderByStartsDesc(int bookerId, LocalDateTime starts,
                                                                                  LocalDateTime ends, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndEndsIsBeforeOrderByStartsDesc(int bookerId, LocalDateTime ends, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStartsIsAfterOrderByStartsDesc(int bookerId, LocalDateTime starts, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStatusOrderByStartsDesc(int bookerId, Status status, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStartsIsBeforeAndEndsIsAfterOrderByStartsDesc(int bookerId, LocalDateTime starts,
                                                                                    LocalDateTime ends, Pageable pageable);


    List<Booking> findBookingByItem_Id(int itemId);

    List<Booking> findBookingByItem_IdAndStatus(int itemId, Status status);

    Page<Booking> findByItemOwnerIdOrderByStartsDesc(int ownerId, Pageable pageable);

    Page<Booking> findBookingsByBooker_IdOrderByStartsDesc(int bookerId, Pageable pageable);


}
