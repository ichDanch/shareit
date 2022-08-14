package ru.practicum.yandex.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.yandex.shareit.exceptions.UserNotFoundException;
import ru.practicum.yandex.shareit.exceptions.ValidationException;
import ru.practicum.yandex.shareit.user.dto.UserDto;
import ru.practicum.yandex.shareit.user.model.User;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UsersRepository usersRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UsersRepository usersRepository, UserMapper userMapper) {
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserDto saveUser(User user) {
        if (user.getEmail() == null) {
            throw new ValidationException("User email must not be null");
        }
        User newUser = usersRepository.save(user);
        return userMapper.toDto(newUser);
    }

    @Transactional
    public UserDto updateUser(UserDto userDto, long id) {
        User user = findById(id);  // тут уже есть проверка на налчиие в базе
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        User patchedUser = usersRepository.save(user);
        return userMapper.toDto(patchedUser);
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
    public void deleteUserById(long id) {
        findById(id);
        usersRepository.deleteById(id);
    }

}
