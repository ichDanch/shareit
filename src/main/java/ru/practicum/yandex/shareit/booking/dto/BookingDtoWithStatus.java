package ru.practicum.yandex.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.yandex.shareit.booking.Status;
import ru.practicum.yandex.shareit.item.model.Item;
import ru.practicum.yandex.shareit.user.model.User;

import java.time.LocalDateTime;

@Data

@NoArgsConstructor
@Builder
public class BookingDtoWithStatus {
    private long id;
    private Status status;
    private long bookerId;
    private long itemId;
    private String itemName;

    public BookingDtoWithStatus(long id, Status status, long bookerId, long itemId, String itemName) {
        this.id = id;
        this.status = status;
        this.bookerId = bookerId;
        this.itemId = itemId;
        this.itemName = itemName;
    }
}

