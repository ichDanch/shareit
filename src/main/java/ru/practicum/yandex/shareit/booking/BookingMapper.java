package ru.practicum.yandex.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.shareit.booking.dto.BookingDto;
import ru.practicum.yandex.shareit.booking.model.Booking;

@Component
public class BookingMapper {

    public BookingDto toDto(Booking booking) {

        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItemId());
    }

    public Booking toBooking(BookingDto bookingDto) {
        return new Booking(
                bookingDto.getItemId(),
                bookingDto.getStart(),
                bookingDto.getEnd()
        );
    }

}
