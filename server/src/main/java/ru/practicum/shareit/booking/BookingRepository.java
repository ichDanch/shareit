package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingsByItemId(long itemId);

    Optional<Booking> findBookingByBookerIdAndEndIsBefore(long bookerId, LocalDateTime now);

    Page<Booking> findBookingsByBooker(User booker, Pageable pageable);

    Page<Booking> findBookingsByItem_Owner(User owner, Pageable pageable);

}
