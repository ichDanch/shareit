package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.State;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@Valid @NotNull @RequestBody BookingDto bookingDto,
                                                @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("createBooking {}. Method [createBooking] class [BookingController]", bookingDto);
        return bookingClient.createBooking(bookingDto, userId);
    }

    @PatchMapping({"/{bookingId}"})
    public ResponseEntity<Object> approveOrRejectBookingByOwner(@PathVariable long bookingId,
                                                                @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId,
                                                                @RequestParam boolean approved) {
        log.info("approveOrRejectBookingByOwner {}. " +
                "Method [approveOrRejectBookingByOwner] class [BookingController]", bookingId);
        return bookingClient.approveOrRejectBookingByOwner(bookingId, userId, approved);
    }

    @GetMapping({"/{bookingId}"})
    public ResponseEntity<Object> findBookingByIdByItemOwnerOrBooker(@PositiveOrZero @PathVariable int bookingId,
                                                                     @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("findBookingByIdByItemOwnerOrBooker {}. " +
                "Method [findBookingByIdByItemOwnerOrBooker] class [BookingController]", bookingId);
        return bookingClient.findBookingByIdByItemOwnerOrBooker(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String state,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        State incomeState = State.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getBookings(userId, incomeState, from, size);
    }


    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingCurrentOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                         @RequestParam(name = "state", defaultValue = "all") String state,
                                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        State incomeState = State.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        log.info("Get booking owner with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getBookingCurrentOwner(userId, incomeState, from, size);
    }
}
