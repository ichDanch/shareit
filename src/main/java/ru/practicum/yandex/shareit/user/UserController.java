package ru.practicum.yandex.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.shareit.user.dto.UserDto;
import ru.practicum.yandex.shareit.user.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public UserDto createUser(@Valid @NotNull @RequestBody User user) {
       return userService.saveUser(user);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PositiveOrZero @PathVariable long id) {
        User getUser = userService.findById(id);
        return userMapper.toDto(getUser);
    }

    @PatchMapping("/{id}")
    public UserDto patch(@Valid @NotNull @RequestBody UserDto userDto,
                         @PathVariable long id) {
        return userService.updateUser(userDto,id);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void delete(@Positive @PathVariable long id) {
        userService.deleteUserById(id);
    }

//    private void EmailDuplicateAndNullValidation(User user) {
//        if (user.getEmail() == null) {
//            throw new ValidationException("User email must not be null");
//        }
//        var emailDuplicateValidation = userServiceJpa.findAll()
//                .stream()
//                .filter(u -> u.getEmail().equals(user.getEmail()))
//                .findFirst();
//        emailDuplicateValidation.ifPresent(u -> {
//            throw new EmailDuplicateException("This email is already exists");
//        });
//    }

//    private void EmailDuplicateAndNullValidationDto(UserDto userDto) {
//
//        var emailDuplicateValidation = userServiceJpa.findAll()
//                .stream()
//                .filter(u -> u.getEmail().equals(userDto.getEmail()))
//                .findFirst();
//        emailDuplicateValidation.ifPresent(u -> {
//            throw new EmailDuplicateException("This email is already exists");
//        });
//    }


}
