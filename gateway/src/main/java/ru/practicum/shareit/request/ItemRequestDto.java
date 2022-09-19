package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.item.ItemDto;

import javax.validation.constraints.Size;
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
    @Size(max = 200, message = "Description must be less then 200 characters")
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items = new ArrayList<>();
}
