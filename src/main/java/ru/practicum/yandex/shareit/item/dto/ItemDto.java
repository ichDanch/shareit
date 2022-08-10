package ru.practicum.yandex.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.*;

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
    //private long owner; // владелец вещи
   // private ItemRequest request; // если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос.

}
