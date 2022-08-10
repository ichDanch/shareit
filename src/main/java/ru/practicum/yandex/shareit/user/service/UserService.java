package ru.practicum.yandex.shareit.user.service;


import ru.practicum.yandex.shareit.user.model.User;

import java.util.List;


public interface UserService {
    User createUser(User user);

    User patchUser(User user);

    List<User> getAllUsers();

    User getUserById(long id);

    void deleteUserById(long id);
}
