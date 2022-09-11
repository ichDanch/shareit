package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.PositiveOrZero;

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
