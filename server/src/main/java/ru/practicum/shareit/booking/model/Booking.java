package ru.practicum.shareit.booking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "bookings")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "start_date")
    LocalDateTime start;
    @Column(name = "end_date")
    @Future
    LocalDateTime end;
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "item_id", nullable = false)
    Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "booker_id", nullable = false)
    User booker;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    Status status;
}
