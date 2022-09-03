package ru.practicum.yandex.shareit.booking.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.yandex.shareit.booking.State;
import ru.practicum.yandex.shareit.booking.dto.BookingDto;
import ru.practicum.yandex.shareit.booking.dto.BookingDtoToUser;

import java.util.List;

public interface BookingService {
    @Transactional
    BookingDto saveBooking(BookingDto bookingDto, long userId);

    @Transactional
    BookingDtoToUser approveOrRejectBookingByOwner(long bookingId, long userId, boolean approved);

    BookingDtoToUser findBookingByIdByItemOwnerOrBooker(long bookingId, long userId);

    List<BookingDtoToUser> findBookingsByCurrentUser(int from, int size, long userId, State state);

    List<BookingDtoToUser> findBookingsByCurrentOwner(int from, int size, long userId, State state);
}
