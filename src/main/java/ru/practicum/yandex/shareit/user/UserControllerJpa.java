package ru.practicum.yandex.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.shareit.exceptions.ValidationException;
import ru.practicum.yandex.shareit.user.dto.UserDto;
import ru.practicum.yandex.shareit.user.model.User;
import ru.practicum.yandex.shareit.user.service.UserServiceJpa;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserControllerJpa {
    private final UserServiceJpa userServiceJpa;
    private final UserMapper userMapper;

    @Autowired
    public UserControllerJpa(UserServiceJpa userServiceJpa, UserMapper userMapper) {
        this.userServiceJpa = userServiceJpa;
        this.userMapper = userMapper;
    }

    @PostMapping
    public UserDto createUser(@Valid @NotNull @RequestBody User user) {

        if (user.getEmail() == null) {
            throw new ValidationException("User email must not be null");
        }
        User newUser = userServiceJpa.save(user);
        return userMapper.toDto(newUser);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PositiveOrZero @PathVariable long id) {
        User getUser = userServiceJpa.findById(id);
        return userMapper.toDto(getUser);
    }

    @PatchMapping("/{id}")
    public UserDto patch(@Valid @NotNull @RequestBody UserDto userDto,
                         @PathVariable long id) {
        //  EmailDuplicateAndNullValidationDto(userDto);
        User user = userServiceJpa.findById(id);  // тут уже есть проверка на налчиие в базе
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        User patchedUser = userServiceJpa.save(user);

        return userMapper.toDto(patchedUser);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userServiceJpa.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void delete(@Positive @PathVariable long id) {
        userServiceJpa.deleteUserById(id);
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
