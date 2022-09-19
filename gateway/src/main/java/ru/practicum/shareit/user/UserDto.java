package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor()
@NoArgsConstructor
@Builder
public class UserDto {
    private long id;
    private String name;
    @Email(message = "Email should be valid")
    private String email;
}
