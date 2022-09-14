package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @NotNull @RequestBody User user) {
        log.info("create user {}. Method [createUser] class [UserController]", user);
        return userClient.createUser(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PositiveOrZero @PathVariable(name = "id") long userId) {
        log.info("getUser {}. Method [getUser] class [UserController]", userId);
        return userClient.getUserById(userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchUser(@Valid @NotNull @RequestBody UserDto userDto,
                                            @PathVariable(name = "id") long userId) {
        log.info("patchUser {}. Method [patchUser] class [UserController]", userId);
        return userClient.updateUser(userDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("getAllUsers. Method [getAllUsers] class [UserController]");
        return userClient.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@Positive @PathVariable(name = "id") long userId) {
        log.info("deleteUser {}. Method [deleteUser] class [UserController]", userId);
        return userClient.deleteUserById(userId);
    }
}
