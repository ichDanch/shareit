package ru.practicum.yandex.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.shareit.user.dto.UserDto;
import ru.practicum.yandex.shareit.user.model.User;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

/*    public User toUser(UserDto userDto) {

        return User.builder()
                .userId(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();

    }*/
}
