package ru.practicum.shareit.user;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    @Transactional
    UserDto saveUser(User user);

    @Transactional
    UserDto updateUser(UserDto userDto, long id);

    List<UserDto> findAll();

    UserDto findById(long id);

    @Transactional
    void deleteUserById(long id);
}
