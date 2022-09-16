package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.item.ItemDto;

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
