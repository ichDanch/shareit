package ru.practicum.yandex.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.yandex.shareit.booking.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    public Booking findBookingsByItemId(long id);
}
