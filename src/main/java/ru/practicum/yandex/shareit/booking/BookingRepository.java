package ru.practicum.yandex.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.yandex.shareit.booking.model.Booking;
import ru.practicum.yandex.shareit.item.model.Item;
import ru.practicum.yandex.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingsByItemId(long itemId);
    Optional<Booking> findBookingByBookerIdAndEndIsBefore(long bookerId, LocalDateTime now);
    Page<Booking> findBookingsByBooker(User booker, Pageable pageable);
    Page<Booking> findBookingsByItem_Owner(User owner, Pageable pageable);

}
