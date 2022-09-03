package ru.practicum.yandex.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.yandex.shareit.exceptions.NotFoundException;
import ru.practicum.yandex.shareit.exceptions.ValidationException;
import ru.practicum.yandex.shareit.user.dto.UserDto;
import ru.practicum.yandex.shareit.user.model.User;
import ru.practicum.yandex.shareit.user.repository.UsersRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    UsersRepository usersRepository;
    @Mock
    UserMapper userMapper;
    UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(usersRepository, userMapper);
    }

    User userOne = new User(1L, "NameOne", "nameone@mail.ru");
    User userOneUpdated = new User(1L, "Updated", "nameone@mail.ru");
    User userTwo = new User(2L, "NameTwo", "nametwo@mail.ru");
    UserDto expectedUserOneDto = new UserDto(1L, "NameOne", "nameone@mail.ru");
    UserDto expectedUserTwoDto = new UserDto(2L, "NameTwo", "nametwo@mail.ru");

    @Test
    void shouldReturnUserDtoWhenSaveUser() {
        when(usersRepository.save(userOne)).thenReturn(userOne);
        when(userMapper.toDto(userOne)).thenReturn(expectedUserOneDto);

        UserDto actualUserDto = userService.saveUser(userOne);

        assertEquals(expectedUserOneDto.getId(), actualUserDto.getId());
        assertEquals(expectedUserOneDto.getName(), actualUserDto.getName());
        assertEquals(expectedUserOneDto.getEmail(), actualUserDto.getEmail());
    }
// написать метод update
//    @Test
//    void shouldReturnUserDtoWhenUpdateUser() {
//        when(usersRepository.findById(1L)).thenReturn(Optional.of(userOne));
//        when(usersRepository.save(any())).thenAnswer(returnsFirstArg());
//
//        var var = userService.updateUser(expectedUserOneDto,1L);
//
//        verify(usersRepository, times(1)).findById(1L);
//        verify(usersRepository, times(1)).save(any());
//
//        assertNotNull(var);
//
//        assertEquals(NEW_NAME, result.getName());
//    }

    @Test
    void shouldReturnValidationExceptionWhenEmailNull() {
        User userWithoutEmail = new User(1, "Name", null);
        assertThrows(ValidationException.class, () -> userService.saveUser(userWithoutEmail));
    }


    @Test
    void shouldFindUserById() {
        when(usersRepository.findById(1L)).thenReturn(Optional.of(userOne));
        when(userMapper.toDto(userOne)).thenReturn(expectedUserOneDto);

        UserDto actualUserDto = userService.findById(1L);
        assertEquals(expectedUserOneDto.getId(), actualUserDto.getId());
        assertEquals(expectedUserOneDto.getName(), actualUserDto.getName());
        assertEquals(expectedUserOneDto.getEmail(), actualUserDto.getEmail());
    }

    @Test
    void shouldFindAllUsers() {
        when(usersRepository.findAll()).thenReturn(List.of(userOne, userTwo));
        when(userMapper.toDto(userOne)).thenReturn(expectedUserOneDto);
        when(userMapper.toDto(userTwo)).thenReturn(expectedUserTwoDto);
        List<UserDto> usersDto = userService.findAll();
        assertNotNull(usersDto);
        assertEquals(2, usersDto.size());
        assertThat(usersDto, equalTo(List.of(expectedUserOneDto, expectedUserTwoDto)));
    }

//    @Test
//    void  shouldDeleteUserById() {
//        //when(usersRepository.deleteById(userOne.getId())).then();
//        userService.deleteUserById(1L);
//       verify(usersRepository, times(1)).deleteById(userOne.getId());
//    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeleteWrongId() {
        assertThrows(NotFoundException.class, () -> userService.deleteUserById(33L));
    }

    @Test
    void shouldThrowNoteFoundExceptionWhenFindWrongId() {
        assertThrows(NotFoundException.class, () -> userService.findById(33L));
    }
}
