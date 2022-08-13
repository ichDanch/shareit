/*
package ru.practicum.yandex.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.shareit.exceptions.EmailDuplicateException;
import ru.practicum.yandex.shareit.exceptions.ValidationException;
import ru.practicum.yandex.shareit.user.dto.UserDto;
import ru.practicum.yandex.shareit.user.model.User;
import ru.practicum.yandex.shareit.user.service.UserService;
import ru.practicum.yandex.shareit.user.service.UserServiceJpa;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserServiceJpa userServiceJpa;
    private UserMapper userMapper;

    @Autowired
    public UserController(UserServiceJpa userServiceJpa, UserMapper userMapper) {
        this.userServiceJpa = userServiceJpa;
        this.userMapper = userMapper;
    }

    @PostMapping
    public UserDto create(@Valid @NotNull @RequestBody User user) {
        */
/*User newUser = userMapper.toUser(userDto);
        userService.create(newUser);
        return userDto;*//*

        //проверка на
        EmailDuplicateAndNullValidation(user);

        userServiceJpa.save(user);
        return userMapper.toDto(user);
    }

   */
/* @PatchMapping("/{id}")
    public UserDto patch(@Valid @NotNull @RequestBody UserDto userDto,
                         @PathVariable long id) {
        EmailDuplicateAndNullValidationDto(userDto);
        User user = userServiceJpa.findOne(id);  // тут уже есть проверка на налчиие в базе
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        User patchedUser = userServiceJpa.patch(user);

        return userMapper.toDto(patchedUser);
    }*//*


    */
/*@DeleteMapping("/{id}")
    public void delete(@Positive @PathVariable long id) {
        userServiceJpa.delete(id);
    }
*//*

    @GetMapping("/{id}")
    public UserDto get(@PositiveOrZero @PathVariable long id) {
        User getUser = userServiceJpa.findOne(id);
        return userMapper.toDto(getUser);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userServiceJpa.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    private void EmailDuplicateAndNullValidation(User user) {
        if (user.getEmail() == null) {
            throw new ValidationException("User email must not be null");
        }
        var emailDuplicateValidation = userServiceJpa.findAll()
                .stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .findFirst();
        emailDuplicateValidation.ifPresent(u -> {
            throw new EmailDuplicateException("This email is already exists");
        });
    }

    private void EmailDuplicateAndNullValidationDto(UserDto userDto) {
       */
/* if (userDto.getEmail() == null) {
            throw new ValidationException("UserDto email must not be null");
        }*//*

        var emailDuplicateValidation = userServiceJpa.findAll()
                .stream()
                .filter(u -> u.getEmail().equals(userDto.getEmail()))
                .findFirst();
        emailDuplicateValidation.ifPresent(u -> {
            throw new EmailDuplicateException("This email is already exists");
        });
    }
}*/
