package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoToItem;
import ru.practicum.shareit.booking.dto.BookingDtoToUser;
import ru.practicum.shareit.booking.model.Booking;

@Component
public class BookingMapper {

    public BookingDto toDto(Booking booking) {

        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId());
    }

    public Booking toBooking(BookingDto bookingDto) {
        return new Booking(
                bookingDto.getStart(),
                bookingDto.getEnd()
        );
    }

    public BookingDtoToUser toBookingDtoToUser(Booking booking) {
        return BookingDtoToUser.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }

    public BookingDtoToItem toBookingDtoToItem(Booking booking) {
        return new BookingDtoToItem(
                booking.getId(),
                booking.getItem().getId(),
                booking.getBooker().getId()
        );
    }


}
