package ru.practicum.yandex.shareit.request.dto;

import lombok.*;
import ru.practicum.yandex.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ItemRequestDto {
    private long id;
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items = new ArrayList<>();
}
