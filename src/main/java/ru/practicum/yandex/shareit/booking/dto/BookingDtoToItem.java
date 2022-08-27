package ru.practicum.yandex.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDtoToItem {

    private long id;

    private long itemId;

    private long bookerId;
}

