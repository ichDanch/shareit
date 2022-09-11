package ru.practicum.yandex.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.yandex.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
public class UserControllerTests {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserServiceImpl userService;

    @Autowired
    private MockMvc mvc;

    UserDto expectedUserOneDto = new UserDto(1L, "NameOne", "nameone@mail.ru");

    @Test
    void shouldReturnUserDtoWhenSaveNewUser() throws Exception {
        when(userService.saveUser(any()))
                .thenReturn(expectedUserOneDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(expectedUserOneDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserOneDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expectedUserOneDto.getName())))
                .andExpect(jsonPath("$.email", is(expectedUserOneDto.getEmail())));
    }

    @Test
    void shouldReturnUserDtoWhenUpdateUser() throws Exception {
        when(userService.updateUser(expectedUserOneDto, 1L))
                .thenReturn(expectedUserOneDto);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(expectedUserOneDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserOneDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expectedUserOneDto.getName())))
                .andExpect(jsonPath("$.email", is(expectedUserOneDto.getEmail())));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mvc.perform(delete("/users/100"))
                .andExpect(status().isOk());
        Mockito.verify(userService, Mockito.times(1))
                .deleteUserById(anyLong());
    }

    @Test
    void shouldFindUserById() throws Exception {
        when(userService.findById(anyLong()))
                .thenReturn(expectedUserOneDto);

        mvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserOneDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expectedUserOneDto.getName())))
                .andExpect(jsonPath("$.email", is(expectedUserOneDto.getEmail())));
    }

    @Test
    void shouldFindAllUsers() throws Exception {
        when(userService.findAll())
                .thenReturn(List.of(expectedUserOneDto));

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(expectedUserOneDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(expectedUserOneDto.getName())))
                .andExpect(jsonPath("$.[0].email", is(expectedUserOneDto.getEmail())));
    }
}
