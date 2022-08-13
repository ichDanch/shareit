package ru.practicum.yandex.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.yandex.shareit.exceptions.UserNotFoundException;
import ru.practicum.yandex.shareit.user.UsersRepository;
import ru.practicum.yandex.shareit.user.model.User;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceJpa {
    private final UsersRepository usersRepository;
    @Autowired
    public UserServiceJpa(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Transactional
    public User save(User user) {
        return usersRepository.save(user);
    }

    public List<User> findAll() {
        return usersRepository.findAll();
    }

    public User findById(long id) {
        return usersRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Does not contain user with this id or id is invalid " + id));
    }
    @Transactional
    public void deleteUserById (long id) {
        findById(id);
        usersRepository.deleteById(id);
    }

}
