package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor()
@NoArgsConstructor
@Builder
public class UserDto {
    @PositiveOrZero
    private long id;
    @NotEmpty
    @NotBlank(message = "Name cannot be null or empty")
    private String name;
    @Email(message = "Email should be valid")
    private String email;
}
