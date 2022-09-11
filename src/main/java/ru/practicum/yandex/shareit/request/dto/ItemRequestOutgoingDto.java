package ru.practicum.yandex.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ItemRequestOutgoingDto {
    private long id;
    private String description;
    private Instant created;
}
