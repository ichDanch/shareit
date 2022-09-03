package ru.practicum.yandex.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.yandex.shareit.item.dto.ItemDto;

import javax.persistence.Transient;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Data
public class ItemRequestDto {
    private long id;
    private String description;
    private Instant created;
    private List<ItemDto> items = new ArrayList<>();

    public ItemRequestDto() {
    }

    public ItemRequestDto(long id, String description, Instant created) {
        this.id = id;
        this.description = description;
        this.created = created;
    }
}
