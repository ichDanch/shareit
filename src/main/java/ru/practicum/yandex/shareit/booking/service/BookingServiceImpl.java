package ru.practicum.yandex.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.yandex.shareit.booking.BookingMapper;
import ru.practicum.yandex.shareit.booking.BookingRepository;
import ru.practicum.yandex.shareit.booking.State;
import ru.practicum.yandex.shareit.booking.Status;
import ru.practicum.yandex.shareit.booking.dto.BookingDto;
import ru.practicum.yandex.shareit.booking.dto.BookingDtoToUser;
import ru.practicum.yandex.shareit.booking.model.Booking;
import ru.practicum.yandex.shareit.exceptions.NotFoundException;
import ru.practicum.yandex.shareit.exceptions.ValidationException;
import ru.practicum.yandex.shareit.item.repository.ItemsRepository;
import ru.practicum.yandex.shareit.item.model.Item;
import ru.practicum.yandex.shareit.user.repository.UsersRepository;
import ru.practicum.yandex.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ItemsRepository itemsRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              BookingMapper bookingMapper,
                              ItemsRepository itemsRepository,
                              UsersRepository usersRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.itemsRepository = itemsRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    @Transactional
    public BookingDto saveBooking(BookingDto bookingDto, long userId) {
        User bookingCreator = checkUser(userId);
        long itemId = bookingDto.getItemId();
        Item item = checkItem(itemId);

        if (item.getOwner().getId() == userId) {
            throw new NotFoundException("Сan not be booked twice");
        }
        if (!item.getAvailable()) {
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

        Booking booking = bookingMapper.toBooking(bookingDto);
        booking.setBooker(bookingCreator);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);

        Booking savingBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(savingBooking);
    }

    @Override
    @Transactional
    public BookingDtoToUser approveOrRejectBookingByOwner(long bookingId, long userId, boolean approved) {
        User user = checkUser(userId);
        Booking booking = checkBooking(bookingId);
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
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return bookingMapper.toBookingDtoToUser(booking);
    }


    @Override
    public BookingDtoToUser findBookingByIdByItemOwnerOrBooker(long bookingId, long userId) {
        Booking booking = checkBooking(bookingId);
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

    @Override
    public List<BookingDtoToUser> findBookingsByCurrentUser(int from, int size, long userId, State state) {

        if (from < 0 || size <= 0) {
            throw new ValidationException("from or size are not valid");
        }
        User currentUser = checkUser(userId);
        Pageable pageWithElements = PageRequest.of(from / size, size, Sort.by("start").descending());
        Page<Booking> page = bookingRepository.findBookingsByBooker(currentUser, pageWithElements);
        List<Booking> bookings = page.get().collect(Collectors.toList());

        return getBookingDtoToUsers(state, bookings);
    }

    @Override
    public List<BookingDtoToUser> findBookingsByCurrentOwner(int from, int size, long userId, State state) {
        if (from < 0 || size <= 0) {
            throw new ValidationException("from or size are not valid");
        }

        User currentOwner = checkUser(userId);
        Pageable pageWithElements = PageRequest.of(from / size, size, Sort.by("start").descending());
        Page<Booking> page = bookingRepository.findBookingsByItem_Owner(currentOwner, pageWithElements);
        List<Booking> bookings = page.get().collect(Collectors.toList());

        if (bookings.isEmpty()) {
            throw new ValidationException("Need at least one thing");
        }
        return getBookingDtoToUsers(state, bookings);
    }

    private List<BookingDtoToUser> getBookingDtoToUsers(State state, List<Booking> bookings) {
        switch (state) {
            case ALL:
                return bookings
                        .stream()
                        .map(bookingMapper::toBookingDtoToUser)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookings
                        .stream()
                        .filter(b -> b.getStart().isBefore(LocalDateTime.now())
                                && b.getEnd().isAfter(LocalDateTime.now()))
                        .map(bookingMapper::toBookingDtoToUser)
                        .collect(Collectors.toList());
            case PAST:
                return bookings
                        .stream()
                        .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                        .map(bookingMapper::toBookingDtoToUser)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookings
                        .stream()
                        .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                        .map(bookingMapper::toBookingDtoToUser)
                        .collect(Collectors.toList());
            case WAITING:
                return bookings
                        .stream()
                        .filter(b -> b.getStatus().equals(Status.WAITING))
                        .map(bookingMapper::toBookingDtoToUser)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookings
                        .stream()
                        .filter(b -> b.getStatus().equals(Status.REJECTED))
                        .map(bookingMapper::toBookingDtoToUser)
                        .collect(Collectors.toList());
            default:
                throw new RuntimeException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    private User checkUser(long userId) {
        return usersRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Does not contain user with this id or id is invalid " + userId));
    }

    private Item checkItem(long itemId) {
        return itemsRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Does not contain item with this id or id is invalid " + itemId));
    }

    private Booking checkBooking(long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Does not contain booking with this id or id is invalid "));
    }

}