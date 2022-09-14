package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createUser(ItemDto itemDto, long userId) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> patchItem(ItemDto itemDto, long itemId, long userId) {
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> getItemById(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getAllItemsByOwnerId(int from, int size, long ownerId) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", ownerId, parameters);
    }

    public ResponseEntity<Object> searchItemsByNameAndDescription(long from, long size, String text) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", null, parameters);
    }

    public ResponseEntity<Object> deleteItem(long itemId, long userId) {
        return delete("/" + itemId, userId);
    }

    public ResponseEntity<Object> createComment(CommentDto commentDto, long itemId, long userId) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }

//    @PostMapping
//    public ResponseEntity<Object> createUser(@Valid @NotNull @RequestBody User user) {
//        log.info("create user {}. Method [createUser] class [UserController]", user);
//        return userClient.createUser(user);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Object> getUser(@PositiveOrZero @PathVariable(name = "id") long userId) {
//        log.info("getUser {}. Method [getUser] class [UserController]", userId);
//        return userClient.getUserById(userId);
//    }
//
//    @PatchMapping("/{id}")
//    public ResponseEntity<Object> patchUser(@Valid @NotNull @RequestBody UserDto userDto,
//                                            @PathVariable(name = "id") long userId) {
//        log.info("patchUser {}. Method [patchUser] class [UserController]", userId);
//        return userClient.updateUser(userDto, userId);
//    }
//
//    @GetMapping
//    public ResponseEntity<Object> getAllUsers() {
//        log.info("getAllUsers. Method [getAllUsers] class [UserController]");
//        return userClient.getAllUsers();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Object> deleteUser(@Positive @PathVariable(name = "id") long userId) {
//        log.info("deleteUser {}. Method [deleteUser] class [UserController]", userId);
//        return userClient.deleteUserById(userId);
//    }
}
