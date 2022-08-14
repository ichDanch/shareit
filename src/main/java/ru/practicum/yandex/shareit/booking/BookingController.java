package ru.practicum.yandex.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.shareit.booking.dto.BookingDto;
import ru.practicum.yandex.shareit.booking.model.Booking;
import ru.practicum.yandex.shareit.exceptions.ItemNotFoundException;
import ru.practicum.yandex.shareit.exceptions.UserNotFoundException;
import ru.practicum.yandex.shareit.exceptions.ValidationException;
import ru.practicum.yandex.shareit.item.model.Item;
import ru.practicum.yandex.shareit.item.ItemService;
import ru.practicum.yandex.shareit.user.UserService;
import ru.practicum.yandex.shareit.user.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;
    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    public BookingController(BookingService bookingService,
                             BookingMapper bookingMapper,
                             UserService userService,
                             ItemService itemService) {
        this.bookingService = bookingService;
        this.bookingMapper = bookingMapper;
        this.userService = userService;
        this.itemService = itemService;
    }

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
        // проверяем start и end на past(прошлое)
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

        Booking savingBooking = bookingService.save(booking);
        return bookingMapper.toDto(savingBooking);
    }

/*    @PatchMapping({"/{bookingId}"})
    public BookingDto approveOrRejectBooking(@PathVariable long bookingId,
                                             @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestParam String approved) {
        // 1. перенести логику в сервис
        // 2. проверку вынести в отделньый метод

        // Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи.
        // Затем статус бронирования становится либо APPROVED, либо REJECTED.
        // Эндпоинт — PATCH /bookings/{bookingId}?approved={approved}, параметр approved
        // может принимать значения true или false.

        // 1. проверка что это владелец вещи
        // 2.
        // есть юзер айди -> берем юзера - берем список его вещей - берем букинг и берем айди вещи - сравниваем с вещами пользователя
        userService.findById(userId); // проверка владельца
        var var = bookingService.findById(bookingId);
        long desiredItemId = bookingService.findById(bookingId).getItemId(); // получили айди вещи
        Item desiredItem = itemService.findAllUserItem(userId) // нашли вещь владельца
                .stream()
                .filter(item -> desiredItemId == item.getId())
                .findFirst()
                .orElseThrow(() ->
                        new ItemNotFoundException("Does not contain item with this id or id is invalid"));

        if (approved.equals("true")) {
            desiredItem.
        }

        return null;

    }*/

    private void bookingValidation(BookingDto bookingDto, long userId) {

    }

//    private void checkOwner(long userId, Item item) {
//        if (item.getOwner() != userId) {
//            throw new UserNotFoundException("Only the owner can change item");
//        }
//    }

    @GetMapping({"/{bookingId}"})
    public BookingDto findBookingById(@PositiveOrZero @PathVariable int bookingId,
                                      @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {
        //Получение данных о конкретном бронировании (включая его статус).
        // Может быть выполнено либо автором бронирования, либо владельцем вещи,
        // к которой относится бронирование. Эндпоинт — GET /bookings/{bookingId}.

        Booking booking = bookingService.findById(bookingId);
        return bookingMapper.toDto(booking);
    }
}
