package ru.practicum.yandex.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.yandex.shareit.exceptions.NotFoundException;
import ru.practicum.yandex.shareit.exceptions.ValidationException;
import ru.practicum.yandex.shareit.user.UserMapper;
import ru.practicum.yandex.shareit.user.repository.UsersRepository;
import ru.practicum.yandex.shareit.user.dto.UserDto;
import ru.practicum.yandex.shareit.user.model.User;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UsersRepository usersRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository, UserMapper userMapper) {
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserDto saveUser(User user) {
        if (user.getEmail() == null) {
            throw new ValidationException("User email must not be null");
        }
        User newUser = usersRepository.save(user);
        return userMapper.toDto(newUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, long id) {
        User user = findById(id);
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        User patchedUser = usersRepository.save(user);
        return userMapper.toDto(patchedUser);
    }

    @Override
    public List<User> findAll() {
        return usersRepository.findAll();
    }

    @Override
    public User findById(long id) {
        return usersRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Does not contain user with this id or id is invalid " + id));
    }

    @Override
    @Transactional
    public void deleteUserById(long id) {
        findById(id);
        usersRepository.deleteById(id);
    }

}
