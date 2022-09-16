package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoToUser;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingServiceImpl bookingServiceImpl;

    @Autowired
    public BookingController(BookingServiceImpl bookingServiceImpl) {
        this.bookingServiceImpl = bookingServiceImpl;
    }

    @PostMapping
    public BookingDto createBooking(@RequestBody BookingDto bookingDto,
                                    @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingServiceImpl.saveBooking(bookingDto, userId);
    }

    @PatchMapping({"/{bookingId}"})
    public BookingDtoToUser approveOrRejectBookingByOwner(@PathVariable long bookingId,
                                                          @RequestHeader("X-Sharer-User-Id") long userId,
                                                          @RequestParam boolean approved) {
        return bookingServiceImpl.approveBookingByOwner(bookingId, userId, approved);
    }

    @GetMapping({"/{bookingId}"})
    public BookingDtoToUser findBookingByIdByItemOwnerOrBooker(@PathVariable int bookingId,
                                                               @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingServiceImpl.findBookingByIdByItemOwnerOrBooker(bookingId, userId);
    }

    @GetMapping()
    List<BookingDtoToUser> findBookingsByCurrentUser(@RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "20") int size,
                                                     @RequestHeader("X-Sharer-User-Id") long userId,
                                                     @RequestParam(defaultValue = "ALL") State state) {
        return bookingServiceImpl.findBookingsByCurrentUser(from, size, userId, state);
    }

    @GetMapping("/owner")
    List<BookingDtoToUser> findBookingsByCurrentOwner(@RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "20") int size,
                                                      @RequestHeader("X-Sharer-User-Id") long userId,
                                                      @RequestParam(defaultValue = "ALL") State state) {
        return bookingServiceImpl.findBookingsByCurrentOwner(from, size, userId, state);
    }
}