package ru.practicum.yandex.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor()
@Builder
public class User {
    @PositiveOrZero
    private long id;
    @NotEmpty
    @NotBlank(message = "Name cannot be null or empty")
    private String name;
    @Email(message = "Email should be valid")
    private String email;
}
