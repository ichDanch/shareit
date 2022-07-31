package ru.practicum.yandex.shareit.user.dao;

import ru.practicum.yandex.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User create(User user);

    User patch(User user);

    List<User> getAllUsers();

    Optional<User> getUser(long id);

    void delete(long id);
}
