package ru.practicum.yandex.shareit.user.service;


import ru.practicum.yandex.shareit.user.model.User;

import java.util.List;


public interface UserService {
    User create(User user);

    User patch(User user);

    List<User> getAllUsers();

    User get(long id);

    void delete(long id);
}
