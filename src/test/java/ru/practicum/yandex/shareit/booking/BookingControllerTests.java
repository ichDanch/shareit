package ru.practicum.yandex.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.yandex.shareit.booking.dto.BookingDto;
import ru.practicum.yandex.shareit.booking.dto.BookingDtoToUser;
import ru.practicum.yandex.shareit.booking.service.BookingServiceImpl;
import ru.practicum.yandex.shareit.item.model.Item;
import ru.practicum.yandex.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTests {
    @MockBean
    BookingServiceImpl bookingService;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    User expectedUserOne = new User(1L, "NameOne", "nameone@mail.ru");
    Item expectedItemOne = new Item(1L,
            "Item",
            "ItemDescription",
            true,
            expectedUserOne,
            null);
    BookingDto expectedBookingDto = new BookingDto(
            1L,
            LocalDateTime.of(2023, 12, 1, 10, 10, 10),
            LocalDateTime.of(2023, 12, 2, 10, 10, 10),
            1L);
    BookingDtoToUser expectedBookingDtoToUser = new BookingDtoToUser(
            2L,
            LocalDateTime.of(2023, 12, 1, 10, 10, 10),
            LocalDateTime.of(2023, 12, 2, 10, 10, 10),
            expectedItemOne,
            expectedUserOne,
            Status.WAITING);

    @Test
    void shouldReturnBookingDtoWhenSaveBooking() throws Exception {
        when(bookingService.saveBooking(any(), anyLong()))
                .thenReturn(expectedBookingDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(expectedBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBookingDto)));
    }

    @Test
    void shouldReturnBookingDtoToUserWhenApprove() throws Exception {

        when(bookingService.approveOrRejectBookingByOwner(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(expectedBookingDtoToUser);

        mvc.perform(patch("/bookings/1?approved=true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBookingDtoToUser)));
    }

    @Test
    void shouldReturnBookingDtoToUserWhenFindBookingByIdByItemOwnerOrBooker() throws Exception {
        when(bookingService.findBookingByIdByItemOwnerOrBooker(anyLong(), anyLong()))
                .thenReturn(expectedBookingDtoToUser);

        mvc.perform(get("/bookings/2")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBookingDtoToUser)));
    }

    @Test
    void shouldReturnBookingDtoToUserWhenFindBookingsByCurrentUser() throws Exception {
        when(bookingService.findBookingsByCurrentUser(anyInt(), anyInt(), anyLong(), any()))
                .thenReturn(List.of(expectedBookingDtoToUser));

        mvc.perform(get("/bookings?state=ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(expectedBookingDtoToUser))));
    }

    @Test
    void shouldReturnBookingDtoToUserWhenFindBookingsByCurrentOwner() throws Exception {
        when(bookingService.findBookingsByCurrentOwner(anyInt(), anyInt(), anyLong(), any()))
                .thenReturn(List.of(expectedBookingDtoToUser));

        mvc.perform(get("/bookings/owner?state=ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(expectedBookingDtoToUser))));
    }

    @Test
    void shouldNotReturnBookingWhenWrongState() throws Exception {
        when(bookingService.findBookingsByCurrentOwner(anyInt(), anyInt(), anyLong(), any()))
                .thenReturn(List.of(expectedBookingDtoToUser));

        mvc.perform(get("/bookings/owner?state=WrongState")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
