package ru.practicum.yandex.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.yandex.shareit.booking.dto.BookingDtoToUser;
import ru.practicum.yandex.shareit.booking.model.Booking;
import ru.practicum.yandex.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingsByBookerOrderByStartDesc(User bookerId);
    List<Booking> findBookingsByItem_OwnerOrderByStartDesc(User owner);

    List<Booking> findBookingsByItemId(long itemId);

//    @Query("select b from Booking b where b.booker.id = 1 and b.end < ")
//    Booking findBookingByBooker (long bookerId, LocalDateTime now);

    Optional<Booking> findBookingByBookerIdAndEndIsBefore(long bookerId, LocalDateTime now);


  //  List<Booking> findBookingsByBookerOrderByStartDesc(User bookerId);
    /*@Transactional
    @Modifying
    @Query ("update Booking set Booking.status = ?2 where Booking.id = ?1")
    void updateStatusByOwner (long bookingId, Status status);*/
}
