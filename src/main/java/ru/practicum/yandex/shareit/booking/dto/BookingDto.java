package ru.practicum.yandex.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private long itemId;
}
