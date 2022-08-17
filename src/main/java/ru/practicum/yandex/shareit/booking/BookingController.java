package ru.practicum.yandex.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.shareit.booking.dto.BookingDto;
import ru.practicum.yandex.shareit.booking.dto.BookingDtoToUser;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto createBooking(@Valid @NotNull @RequestBody BookingDto bookingDto,
                                    @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.saveBooking(bookingDto, userId);
    }

    @PatchMapping({"/{bookingId}"})
    public BookingDtoToUser approveOrRejectBookingByOwner(@PathVariable long bookingId,
                                                          @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId,
                                                          @RequestParam boolean approved) {
        return bookingService.approveOrRejectBookingByOwner(bookingId, userId, approved);
    }

    @GetMapping({"/{bookingId}"})
    public BookingDtoToUser findBookingByIdByItemOwnerOrBooker(@PositiveOrZero @PathVariable int bookingId,
                                                               @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.findBookingByIdByItemOwnerOrBooker(bookingId, userId);
    }

    //получение списка ВСЕХ БРОНИРОВАНИЙ текущего пользователя
    @GetMapping()
    List<BookingDtoToUser> findBookingsByCurrentUser(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                                                    @RequestParam(defaultValue = "ALL") State state) {

        return bookingService.findBookingsByCurrentUser(userId,state);
    }

    @GetMapping("/owner")
    List<BookingDtoToUser> findBookingsByCurrentOwner(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.findBookingsByCurrentOwner(userId,state);
    }
}






   /*
   Копия рабочего метода
   @PostMapping
    public BookingDto createBooking(@Valid @NotNull @RequestBody BookingDto bookingDto,
                                    @RequestHeader("X-Sharer-User-Id") long userId) {
        // 1. Запрос может быть создан любым пользователем
        // 2. Проверить вещь на доступность available/unavailable
        // 3. а затем подтверждён владельцем вещи
        // 4. После создания запрос находится в статусе WAITING

        User  bookingCreator = userService.findById(userId);               //проверяем наличие пользователя

        long itemId = bookingDto.getItemId();
        Item item = itemService.findById(itemId);  // проверяем наличие вещи
        if (!item.getAvailable()) {                 // проверяем доступность вещи available/unavailable
            throw new ValidationException("Currently unavailable");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now()) || bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Is it time machine ?");
        }
        if (bookingDto.getStart() == null) {
            throw new ValidationException("bookingDto start must not be null");
        }
        if (bookingDto.getEnd() == null) {
            throw new ValidationException("bookingDto end must not be null");
        }

        Booking booking = bookingMapper.toBooking(bookingDto);  // преобразуем дто в объект
        booking.setBooker(bookingCreator);                      // присвоили создателя бронирования
        booking.setItem(item);
        //  booking.setBookerId(userId);                            // присвоили создателя бронирования
        booking.setStatus(Status.WAITING);                      // присвоили статус "ожидает подтверждения"

        Booking savingBooking = bookingService.saveBooking(booking);
        return bookingMapper.toDto(savingBooking);
    }*/