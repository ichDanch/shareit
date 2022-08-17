package ru.practicum.yandex.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.yandex.shareit.booking.dto.BookingDtoToUser;
import ru.practicum.yandex.shareit.booking.model.Booking;
import ru.practicum.yandex.shareit.user.model.User;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingsByBookerOrderByStartDesc(User bookerId);
    List<Booking> findBookingsByItem_OwnerOrderByStartDesc(User owner);


  //  List<Booking> findBookingsByBookerOrderByStartDesc(User bookerId);
    /*@Transactional
    @Modifying
    @Query ("update Booking set Booking.status = ?2 where Booking.id = ?1")
    void updateStatusByOwner (long bookingId, Status status);*/
}
