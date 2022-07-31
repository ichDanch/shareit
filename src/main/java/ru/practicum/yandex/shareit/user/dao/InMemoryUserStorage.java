package ru.practicum.yandex.shareit.user.dao;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Long, User> users = new HashMap<>();
    private long USER_COUNT;

    @Override
    public User create(User user) {
        user.setId(++USER_COUNT);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User patch(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUser(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void delete(long id) {
        users.remove(id);
    }
}
