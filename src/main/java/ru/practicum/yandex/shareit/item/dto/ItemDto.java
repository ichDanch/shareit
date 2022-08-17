package ru.practicum.yandex.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.yandex.shareit.booking.dto.BookingDtoToItem;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor()
@NoArgsConstructor
@Builder
public class ItemDto {
    @PositiveOrZero
    private long id;
    private String name;
    @Size(max = 200, message = "Description must be less then 200 characters")
    private String description;
    private Boolean available; // Статус должен проставлять владелец
    private BookingDtoToItem nextBooking;
    private BookingDtoToItem lastBooking;
    //private long owner; // владелец вещи
   // private ItemRequest request; // если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос.

    public ItemDto(long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
