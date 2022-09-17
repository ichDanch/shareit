package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor()
@NoArgsConstructor
@Builder
public class UserDto {
    @PositiveOrZero
    private long id;
    private String name;
    @Email(message = "Email should be valid")
    private String email;
}
