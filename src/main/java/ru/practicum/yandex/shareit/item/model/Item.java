package ru.practicum.yandex.shareit.item.model;

import lombok.Data;
import ru.practicum.yandex.shareit.request.ItemRequest;

import javax.validation.constraints.*;

@Data
public class Item {
    @PositiveOrZero
    private long id;
    @NotEmpty
    @NotBlank(message = "Name cannot be null or empty")
    private String name;
    @Size(max = 200, message = "Description must be less then 200 characters")
    private String description;
    @NotNull
    private Boolean available; // Статус должен проставлять владелец
    private long owner; // владелец вещи
    private ItemRequest request; // если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос.

    public Item(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }
    public Item(String name, String description, Boolean available, long owner) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }
}
