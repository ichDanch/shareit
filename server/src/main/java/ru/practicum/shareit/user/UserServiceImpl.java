package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UsersRepository;

import java.util.List;
import java.util.stream.Collectors;

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
        User user = usersRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Does not contain user with this id or id is invalid " + id));
        ;
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
    public List<UserDto> findAll() {
        return usersRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDto findById(long id) {
        User user = usersRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Does not contain user with this id or id is invalid " + id));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void deleteUserById(long id) {
        findById(id);
        usersRepository.deleteById(id);
    }

}
