package ru.practicum.yandex.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.yandex.shareit.booking.dto.BookingDto;
import ru.practicum.yandex.shareit.booking.dto.BookingDtoToUser;
import ru.practicum.yandex.shareit.booking.model.Booking;
import ru.practicum.yandex.shareit.exceptions.NotFoundException;
import ru.practicum.yandex.shareit.exceptions.StateException;
import ru.practicum.yandex.shareit.exceptions.ValidationException;
import ru.practicum.yandex.shareit.item.ItemService;
import ru.practicum.yandex.shareit.item.model.Item;
import ru.practicum.yandex.shareit.user.UserService;
import ru.practicum.yandex.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          BookingMapper bookingMapper,
                          UserService userService,
                          ItemService itemService) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Transactional
    public BookingDto saveBooking(BookingDto bookingDto, long userId) {
        // 1. Запрос может быть создан любым пользователем
        // 2. Проверить вещь на доступность available/unavailable
        // 3. а затем подтверждён владельцем вещи
        // 4. После создания запрос находится в статусе WAITING

        User bookingCreator = userService.findById(userId);           //проверяем наличие пользователя

        long itemId = bookingDto.getItemId();
        Item item = itemService.findById(itemId);                     // проверяем наличие вещи

        if (item.getOwner().getId() == userId) {
            throw new NotFoundException("Сan not be booked twice");
        }
        if (!item.getAvailable()) {                                    // проверяем доступность вещи available/unavailable
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
        booking.setItem(item);                                  // присвоили вещь
        booking.setStatus(Status.WAITING);                      // присвоили статус "ожидает подтверждения"

        Booking savingBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(savingBooking);
    }

    @Transactional
    public BookingDtoToUser approveOrRejectBookingByOwner(long bookingId, long userId, boolean approved) {
        User user = userService.findById(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Does not contain booking with this id or id is invalid "));
        Item item = booking.getItem();
        User owner = item.getOwner();
        long ownerId = owner.getId();
        if (ownerId != userId) {
            throw new NotFoundException("Change status can only owner");
        }
        if (approved & booking.getStatus().equals(Status.APPROVED)) {
            throw new ValidationException("Status is already " + Status.APPROVED);
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);

            //bookingRepository.updateStatusByOwner(bookingId,Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
            // bookingRepository.updateStatusByOwner(bookingId,Status.REJECTED);
        }
        return bookingMapper.toBookingDtoToUser(booking);  // может быть bookingRepository.save(booking) ?
    }


    public BookingDtoToUser findBookingByIdByItemOwnerOrBooker(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new NotFoundException("Does not contain booking with this id or id is invalid " + bookingId));
        Item bookingItem = booking.getItem();
        User itemOwner = bookingItem.getOwner();
        long itemOwnerId = itemOwner.getId();

        User booker = booking.getBooker();
        long bookerId = booker.getId();
        if (userId != itemOwnerId && userId != bookerId) {
            throw new NotFoundException("Only owner or booker can get this booking");
        }
        return bookingMapper.toBookingDtoToUser(booking);
    }

    public List<BookingDtoToUser> findBookingsByCurrentUser(long userId, State state) {
        User currentUser = userService.findById(userId);
        switch (state) {
            case ALL:
                return bookingRepository.findBookingsByBookerOrderByStartDesc(currentUser)
                        .stream()
                        .map(bookingMapper::toBookingDtoToUser)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findBookingsByBookerOrderByStartDesc(currentUser)
                        .stream()
                        .filter(b -> b.getStart().isBefore(LocalDateTime.now())
                                && b.getEnd().isAfter(LocalDateTime.now()))
                        .map(bookingMapper::toBookingDtoToUser)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findBookingsByBookerOrderByStartDesc(currentUser)
                        .stream()
                        .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                        .map(bookingMapper::toBookingDtoToUser)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findBookingsByBookerOrderByStartDesc(currentUser)
                        .stream()
                        .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                        .map(bookingMapper::toBookingDtoToUser)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findBookingsByBookerOrderByStartDesc(currentUser)
                        .stream()
                        .filter(b -> b.getStatus().equals(Status.WAITING))
                        .map(bookingMapper::toBookingDtoToUser)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findBookingsByBookerOrderByStartDesc(currentUser)
                        .stream()
                        .filter(b -> b.getStatus().equals(Status.REJECTED))
                        .map(bookingMapper::toBookingDtoToUser)
                        .collect(Collectors.toList());
//           case UNSUPPORTED_STATUS:
//                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
            default:
                throw new StateException("Unknown state: UNSUPPORTED_STATUS");
        }


        //.sorted(Comparator.comparing(BookingDtoToUser::getStart).reversed())
    }

    List<BookingDtoToUser> findBookingsByCurrentOwner(long userId,
                                                      State state) {
        User currentOwner = userService.findById(userId);
        List<Item> items = itemService.findAllOwnersItems(userId);
        List<Booking> bookings = bookingRepository.findBookingsByItem_OwnerOrderByStartDesc(currentOwner);
        if (bookings.isEmpty()) {
            throw new ValidationException("Need at least one thing");
        }
        switch (state) {
            case ALL:
                return bookings.stream()
                        .map(bookingMapper::toBookingDtoToUser)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookings.stream()
                        .filter(b -> b.getStart().isBefore(LocalDateTime.now())
                                && b.getEnd().isAfter(LocalDateTime.now()))
                        .map(bookingMapper::toBookingDtoToUser)
                        .collect(Collectors.toList());
            case PAST:
                return bookings.stream()
                        .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                        .map(bookingMapper::toBookingDtoToUser)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookings.stream()
                        .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                        .map(bookingMapper::toBookingDtoToUser)
                        .collect(Collectors.toList());
            case WAITING:
                return bookings.stream()
                        .filter(b -> b.getStatus().equals(Status.WAITING))
                        .map(bookingMapper::toBookingDtoToUser)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookings.stream()
                        .filter(b -> b.getStatus().equals(Status.REJECTED))
                        .map(bookingMapper::toBookingDtoToUser)
                        .collect(Collectors.toList());
//            case UNSUPPORTED_STATUS:
//                throw new ValidationException("Unknown state");
            default:
                throw new StateException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}