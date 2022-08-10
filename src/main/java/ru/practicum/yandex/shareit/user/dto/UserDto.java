package ru.practicum.yandex.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor()
@Builder
public class UserDto {
    @PositiveOrZero
    private long id;
    private String name;
    @Email(message = "Email should be valid")
    private String email;
}
