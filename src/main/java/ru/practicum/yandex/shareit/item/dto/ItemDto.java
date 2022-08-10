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
    private Boolean available;
}
