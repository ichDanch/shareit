package ru.practicum.yandex.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.yandex.shareit.booking.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table (name = "bookings")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "booking_id")
    private long id;
    @Column(name = "start_date")
    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime start;
    @Column(name = "end_date")
   // @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime end;
    @Column (name ="item_id")
    private long itemId;
    @Column (name = "booker_id")
    private long bookerId;
    @Column (name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public Booking(long itemId, LocalDateTime start, LocalDateTime end) {
        this.itemId = itemId;
        this.start = start;
        this.end = end;
    }
}