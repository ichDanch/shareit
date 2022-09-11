package ru.practicum.shareit.booking.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoToUser;

import java.util.List;

public interface BookingService {
    @Transactional
    BookingDto saveBooking(BookingDto bookingDto, long userId);

    @Transactional
    BookingDtoToUser approveBookingByOwner(long bookingId, long userId, boolean approved);

    BookingDtoToUser findBookingByIdByItemOwnerOrBooker(long bookingId, long userId);

    List<BookingDtoToUser> findBookingsByCurrentUser(int from, int size, long userId, State state);

    List<BookingDtoToUser> findBookingsByCurrentOwner(int from, int size, long userId, State state);
}
