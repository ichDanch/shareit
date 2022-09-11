package ru.practicum.yandex.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.yandex.shareit.booking.dto.BookingDto;
import ru.practicum.yandex.shareit.booking.dto.BookingDtoToUser;
import ru.practicum.yandex.shareit.booking.service.BookingServiceImpl;
import ru.practicum.yandex.shareit.exceptions.NotFoundException;
import ru.practicum.yandex.shareit.exceptions.ValidationException;
import ru.practicum.yandex.shareit.item.dto.ItemDto;
import ru.practicum.yandex.shareit.item.model.Item;
import ru.practicum.yandex.shareit.item.service.ItemServiceImpl;
import ru.practicum.yandex.shareit.user.UserServiceImpl;
import ru.practicum.yandex.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTests {

    @Autowired
    private final BookingServiceImpl bookingService;
    @Autowired
    private final UserServiceImpl userService;
    @Autowired
    private final ItemServiceImpl itemService;
    User userOne = new User(1L, "NameOne", "nameone@mail.ru");
    User userTwo = new User(2L, "NameTwo", "nametwo@mail.ru");
    User userThree = new User(3L, "NameThree", "namethree@mail.ru");
    Item itemOne = new Item(1L,
            "ItemOne",
            "ItemOneDescription",
            true,
            userOne,
            null);
    Item itemTwo = new Item(2L,
            "ItemTwo",
            "ItemTwoDescription",
            true,
            userTwo,
            null);
    ItemDto itemDtoOne = new ItemDto(1L,
            "ItemOne",
            "ItemOneDescription",
            true,
            0,
            new ArrayList<>());
    ItemDto itemDtoTwo = new ItemDto(2L,
            "ItemTwo",
            "ItemTwoDescription",
            false,
            0,
            new ArrayList<>());
    BookingDto bookingDtoOne = new BookingDto(
            1L,
            LocalDateTime.of(2023, 12, 1, 10, 10, 10),
            LocalDateTime.of(2023, 12, 2, 10, 10, 10),
            1L);
    BookingDto bookingDtoTwo = new BookingDto(
            2L,
            LocalDateTime.of(2023, 12, 3, 10, 10, 10),
            LocalDateTime.of(2023, 12, 4, 10, 10, 10),
            2L);

    BookingDtoToUser expectedBookingDtoToUserOne = new BookingDtoToUser(
            1L,
            LocalDateTime.of(2023, 12, 1, 10, 10, 10),
            LocalDateTime.of(2023, 12, 2, 10, 10, 10),
            itemOne,
            userTwo,
            Status.WAITING);
    BookingDtoToUser expectedBookingDtoToUserApproved = new BookingDtoToUser(
            1L,
            LocalDateTime.of(2023, 12, 1, 10, 10, 10),
            LocalDateTime.of(2023, 12, 2, 10, 10, 10),
            itemOne,
            userTwo,
            Status.APPROVED);

    List<BookingDtoToUser> expectedBookings = List.of(expectedBookingDtoToUserOne);


    @Test
    @Order(1)
    void shouldReturnBookingDtoWhenSaveBooking() {
        //given
        long userOneId = userService.saveUser(userOne).getId();
        long itemId = itemService.saveItem(itemDtoOne, userOneId).getId();
        long userTwoId = userService.saveUser(userTwo).getId();
        long bookingId = bookingService.saveBooking(bookingDtoOne, userTwoId).getId();
        //when
        BookingDtoToUser expectedBookingDto = bookingService.findBookingByIdByItemOwnerOrBooker(1L, 2L);
        //then
        assertThat(expectedBookingDto, equalTo(expectedBookingDtoToUserOne));
    }

    @Test
    @Order(2)
    void shouldReturnNotFoundExceptionWhenSaveBookingAndItemOwnerEqualsUser() {
        assertThrows(NotFoundException.class, () -> bookingService.saveBooking(bookingDtoOne, 1L));
    }


    @Test
    @Order(3)
    void shouldReturnValidationExceptionWhenSaveBookingAndIsAvailableFalse() {
        long userOneId = userService.saveUser(userOne).getId();
        long itemIdOne = itemService.saveItem(itemDtoOne, userOneId).getId();
        long itemIdTwo = itemService.saveItem(itemDtoTwo, userOneId).getId();
        long userTwoId = userService.saveUser(userTwo).getId();

        assertThrows(ValidationException.class, () -> bookingService.saveBooking(bookingDtoTwo, userTwoId));
    }

    @Test
    @Order(4)
    void shouldReturnBookingDtoToUserWhenApproveBookingByOwner() {
        long userOneId = userService.saveUser(userOne).getId();
        long itemOneId = itemService.saveItem(itemDtoOne, userOneId).getId();
        long itemTwoId = itemService.saveItem(itemDtoTwo, userOneId).getId();
        long userTwoId = userService.saveUser(userTwo).getId();
        long bookingOneId = bookingService.saveBooking(bookingDtoOne, userTwoId).getId();

        BookingDtoToUser expectedBDTO = bookingService.approveBookingByOwner(bookingOneId, userOneId, true);

        assertThat(expectedBDTO, equalTo(expectedBookingDtoToUserApproved));
    }

    @Test
    @Order(5)
    void shouldReturnValidationExceptionWhenApproveApprovedBookingByOwner() {
        long userOneId = userService.saveUser(userOne).getId();
        long itemOneId = itemService.saveItem(itemDtoOne, userOneId).getId();
        long itemTwoId = itemService.saveItem(itemDtoTwo, userOneId).getId();
        long userTwoId = userService.saveUser(userTwo).getId();
        long bookingOneId = bookingService.saveBooking(bookingDtoOne, userTwoId).getId();
        BookingDtoToUser approvedBDTO = bookingService.approveBookingByOwner(bookingOneId, userOneId, true);

        assertThrows(ValidationException.class,
                () -> bookingService.approveBookingByOwner(bookingOneId, userOneId, true));
    }


    @Test
    @Order(6)
    void shouldReturnBookingDtoToUserWhenFindBookingByIdByItemOwnerOrBooker() {
        long userOneId = userService.saveUser(userOne).getId();
        long itemOneId = itemService.saveItem(itemDtoOne, userOneId).getId();
        long itemTwoId = itemService.saveItem(itemDtoTwo, userOneId).getId();
        long userTwoId = userService.saveUser(userTwo).getId();
        long bookingOneId = bookingService.saveBooking(bookingDtoOne, userTwoId).getId();

        BookingDtoToUser bookingByOwner = bookingService.findBookingByIdByItemOwnerOrBooker(bookingOneId, userTwoId);

        assertThat(expectedBookingDtoToUserOne, equalTo(bookingByOwner));
    }


    @Test
    @Order(7)
    void shouldReturnNotFoundExceptionWhenFindBookingByIdByItemOwnerOrBookerAndUserNotOwnerOrBooker() {
        long userOneId = userService.saveUser(userOne).getId();
        long itemOneId = itemService.saveItem(itemDtoOne, userOneId).getId();
        long itemTwoId = itemService.saveItem(itemDtoTwo, userOneId).getId();
        long userTwoId = userService.saveUser(userTwo).getId();
        long bookingOneId = bookingService.saveBooking(bookingDtoOne, userTwoId).getId();
        long userThreeId = userService.saveUser(userThree).getId();

        assertThrows(NotFoundException.class,
                () -> bookingService.findBookingByIdByItemOwnerOrBooker(bookingOneId, userThreeId));
    }

    @Test
    @Order(8)
    void shouldReturnListOfBookingDtoToUserWhenFindBookingsByCurrentUser() {
        long userOneId = userService.saveUser(userOne).getId();
        long itemOneId = itemService.saveItem(itemDtoOne, userOneId).getId();
        long itemTwoId = itemService.saveItem(itemDtoTwo, userOneId).getId();
        long userTwoId = userService.saveUser(userTwo).getId();
        long bookingOneId = bookingService.saveBooking(bookingDtoOne, userTwoId).getId();
        long userThreeId = userService.saveUser(userThree).getId();

        List<BookingDtoToUser> bookings = bookingService.findBookingsByCurrentUser(0, 20, userTwoId, State.ALL);

        assertEquals(expectedBookings, bookings);
    }

    @Test
    @Order(9)
    void shouldReturnListOfBookingDtoToUserWhenFindBookingsByOwner() {
        long userOneId = userService.saveUser(userOne).getId();
        long itemOneId = itemService.saveItem(itemDtoOne, userOneId).getId();
        long itemTwoId = itemService.saveItem(itemDtoTwo, userOneId).getId();
        long userTwoId = userService.saveUser(userTwo).getId();
        long bookingOneId = bookingService.saveBooking(bookingDtoOne, userTwoId).getId();
        long userThreeId = userService.saveUser(userThree).getId();

        List<BookingDtoToUser> bookings = bookingService.findBookingsByCurrentOwner(0, 20, userOneId, State.ALL);

        assertEquals(expectedBookings, bookings);
    }
}
