package ru.practicum.yandex.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.shareit.user.dto.UserDto;
import ru.practicum.yandex.shareit.user.model.User;
import ru.practicum.yandex.shareit.user.service.UserServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl userServiceImpl;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl, UserMapper userMapper) {
        this.userServiceImpl = userServiceImpl;
        this.userMapper = userMapper;
    }

    @PostMapping
    public UserDto createUser(@Valid @NotNull @RequestBody User user) {
        return userServiceImpl.saveUser(user);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PositiveOrZero @PathVariable long id) {
        User getUser = userServiceImpl.findById(id);
        return userMapper.toDto(getUser);
    }

    @PatchMapping("/{id}")
    public UserDto patch(@Valid @NotNull @RequestBody UserDto userDto,
                         @PathVariable long id) {
        return userServiceImpl.updateUser(userDto, id);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userServiceImpl.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void delete(@Positive @PathVariable long id) {
        userServiceImpl.deleteUserById(id);
    }
}
