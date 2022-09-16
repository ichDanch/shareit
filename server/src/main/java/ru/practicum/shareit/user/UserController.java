package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping
    public UserDto createUser(@RequestBody User user) {
        return userServiceImpl.saveUser(user);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable long id) {
        return userServiceImpl.findById(id);
    }

    @PatchMapping("/{id}")
    public UserDto patch(@RequestBody UserDto userDto,
                         @PathVariable long id) {
        return userServiceImpl.updateUser(userDto, id);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userServiceImpl.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        userServiceImpl.deleteUserById(id);
    }
}
