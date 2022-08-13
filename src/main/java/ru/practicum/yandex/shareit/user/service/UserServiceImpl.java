/*
package ru.practicum.yandex.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.yandex.shareit.exceptions.UserNotFoundException;
import ru.practicum.yandex.shareit.user.dao.UserStorage;
import ru.practicum.yandex.shareit.user.model.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }

    @Override
    public User patch(User user) {
        get(user.getId());
        return userStorage.patch(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User get(long id) {
        return userStorage.getUser(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Does not contain user with this id or id is invalid " + id));
    }

    @Override
    public void delete(long id) {
        userStorage.delete(id);
    }
}
*/
