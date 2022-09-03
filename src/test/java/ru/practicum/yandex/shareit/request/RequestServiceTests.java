package ru.practicum.yandex.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.yandex.shareit.item.dto.ItemDto;
import ru.practicum.yandex.shareit.item.model.Item;
import ru.practicum.yandex.shareit.item.service.ItemServiceImpl;
import ru.practicum.yandex.shareit.request.dto.ItemRequestDto;
import ru.practicum.yandex.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.yandex.shareit.user.UserServiceImpl;
import ru.practicum.yandex.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceTests {
    @Autowired
    private final ItemRequestServiceImpl itemRequestService;
    @Autowired
    private final UserServiceImpl userService;
    @Autowired
    private final ItemServiceImpl itemService;
    User userOne = new User(1L, "NameOne", "nameone@mail.ru");
    User userTwo = new User(2L, "NameTwo", "nametwo@mail.ru");
    ItemRequestDto itemRequestDto = new ItemRequestDto(
            1L,
            "Description",
            LocalDateTime.now()
                    .withNano(0)
                    .toInstant(ZoneOffset.UTC),
            null);
    Item itemOne = new Item(1L,
            "ItemOne",
            "ItemOneDescription",
            true,
            userOne,
            null);
    ItemDto itemDtoOne = new ItemDto(1L,
            "ItemOne",
            "ItemOneDescription",
            true,
            1L,
            new ArrayList<>());

    @Test
    @Order(1)
    void shouldReturnItemRequestDtoWhenSaveItemRequest() {
        long userOneId = userService.saveUser(userOne).getId();

        ItemRequestDto savedItemRequestDto = itemRequestService.saveItemRequest(itemRequestDto, userOneId);


        assertEquals(itemRequestDto.getId(), savedItemRequestDto.getId());
        assertEquals(itemRequestDto.getDescription(), savedItemRequestDto.getDescription());
        assertEquals(itemRequestDto.getItems(), savedItemRequestDto.getItems());
    }

//    @Test
//    @Order(2)
//    void shouldReturnItemRequestDtoWhenFindRequestsByOwner() {
//        long userOneId = userService.saveUser(userOne).getId();
//        long requestDtoId = itemRequestService.saveItemRequest(itemRequestDto,userOneId).getId();
//        long itemId = itemService.saveItem(itemDtoOne, userOneId).getId();
//
//        ItemRequestDto findRequest = itemRequestService.findRequestById(userOneId,requestDtoId);
//
//        assertEquals(itemRequestDto.getId(), findRequest.getId());
//        assertEquals(itemRequestDto.getDescription(), findRequest.getDescription());
//        assertEquals(itemRequestDto.getItems(), findRequest.getItems());
//    }
}
