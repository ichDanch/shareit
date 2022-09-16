package ru.practicum.shareit;

import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//@SpringBootTest
@SpringBootTest
class ShareitServerTests {
//    @Autowired
//    ObjectMapper mapper;
//
//    @MockBean
//    UserServiceImpl userService;
//
//    @Autowired
//    private MockMvc mvc;
//
//    private final UserDto userDto1 = UserDto.builder()
//            .id(1L)
//            .name("User1")
//            .email("user1@mail.ru")
//            .build();
//
//    @Test
//    void testCreateUser() throws Exception {
//        when(userService.saveUser(any()))
//                .thenReturn(userDto1);
//
//        mvc.perform(post("/users")
//                        .content(mapper.writeValueAsString(userDto1))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(userDto1.getId()), Long.class))
//                .andExpect(jsonPath("$.name", is(userDto1.getName())))
//                .andExpect(jsonPath("$.email", is(userDto1.getEmail())));
//    }

}
