package ru.practicum.yandex.shareit.request;

import lombok.Data;
import ru.practicum.yandex.shareit.user.model.User;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.Instant;

@Data
public class ItemRequest {
    @Positive
    private long id;
    @Size(max = 200, message = "Description must be less then 200 characters")
    private String description;
    private User requestor;
    private final Instant create = Instant.now();
}
