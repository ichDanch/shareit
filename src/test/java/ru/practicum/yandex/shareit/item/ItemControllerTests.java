package ru.practicum.yandex.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.yandex.shareit.item.dto.CommentDto;
import ru.practicum.yandex.shareit.item.dto.ItemDto;
import ru.practicum.yandex.shareit.item.mapper.ItemMapper;
import ru.practicum.yandex.shareit.item.service.ItemServiceImpl;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTests {
    @MockBean
    ItemServiceImpl itemService;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    ItemMapper itemMapper;

    ItemDto expectedItemDtoOne = new ItemDto(1L, "ItemOne", "ItemOneDescription", true, 0, new ArrayList<>());
    ItemDto expectedItemDtoTwo = new ItemDto(2L, "ItemTwo", "ItemTwoDescription", true, 0, new ArrayList<>());
    CommentDto expectedCommentDto = new CommentDto(1L, "CommentDto", "Author", LocalDateTime.now());
    @Test
    void shouldReturnItemDtoWhenCreateItem() throws Exception {
        when(itemService.saveItem(any(), anyLong()))
                .thenReturn(expectedItemDtoOne);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(expectedItemDtoOne))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedItemDtoOne)));
    }

    @Test
    void shouldReturnItemDtoWhenUpdateItem() throws Exception {
        when(itemService.patchItem(any(), anyLong(), anyLong()))
                .thenReturn(expectedItemDtoTwo);

        mvc.perform(patch("/items/2")
                        .content(mapper.writeValueAsString(expectedItemDtoTwo))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedItemDtoTwo)));

    }

    @Test
    void shouldFindAllItemsByOwnerId() throws Exception {
        when(itemService.findAllItemsByOwnerId(anyInt(), anyInt(), anyLong()))
                .thenReturn(List.of(expectedItemDtoOne, expectedItemDtoTwo));

        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(expectedItemDtoOne, expectedItemDtoTwo))));
    }

    @Test
    void shouldSearchItemsByNameAndDescription() throws Exception {
        when(itemService.itemsByNameAndDescription(anyInt(), anyInt(), anyString()))
                .thenReturn(List.of(expectedItemDtoOne, expectedItemDtoTwo));

        mvc.perform(get("/items/search?text=item1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(expectedItemDtoOne, expectedItemDtoTwo))));
    }
    @Test
    void shouldReturnCommentDtoWhenCreateComment() throws Exception {
        when(itemService.createCommentByUser(any(), anyLong(), anyLong()))
                .thenReturn(expectedCommentDto);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(expectedCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedCommentDto)));
    }
}
