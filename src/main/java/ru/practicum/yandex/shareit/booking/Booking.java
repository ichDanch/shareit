package ru.practicum.yandex.shareit.booking;

import lombok.Data;
import ru.practicum.yandex.shareit.item.model.Item;
import ru.practicum.yandex.shareit.user.model.User;

import java.time.LocalDate;

@Data
public class Booking {
    private long id;
    private LocalDate start;
    private LocalDate end;
    private Item item;
    private User booker; // пользователь, который осуществляет бронирование;
    private Status status;
}
