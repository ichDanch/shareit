package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;


    @PostMapping
    public ResponseEntity<Object> createItem(@Valid @RequestBody ItemDto itemDto,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("createItem {}. Method [createItem] class [ItemController]", itemDto);
        return itemClient.createItem(itemDto, userId);

    }

    @PatchMapping({"/{itemId}"})
    public ResponseEntity<Object> patchItem(@RequestBody ItemDto itemDto,
                                            @PathVariable long itemId,
                                            @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("patchItem {}. Method [patchItem] class [ItemController]", itemDto);
        return itemClient.patchItem(itemDto, itemId, userId);
    }

    @GetMapping({"/{itemId}"})
    public ResponseEntity<Object> findItemById(@PathVariable long itemId,
                                               @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("findItemById {}. Method [findItemById] class [ItemController]", itemId);
        return itemClient.getItemById(itemId, userId);

    }

    @GetMapping
    public ResponseEntity<Object> findAllItemsByOwnerId(@Valid @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "20") int size,
                                                        @RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.info("findAllItemsByOwnerId {}. Method [findAllItemsByOwnerId] class [ItemController]", ownerId);
        return itemClient.getAllItemsByOwnerId(from, size, ownerId);
    }

    @GetMapping({"/search"})
    public ResponseEntity<Object> searchItemsByNameAndDescription(@Valid @RequestParam(defaultValue = "0") int from,
                                                                  @RequestParam(defaultValue = "20") int size,
                                                                  @RequestParam String text) {
        log.info("searchItemsByNameAndDescription {}. " +
                "Method [searchItemsByNameAndDescription] class [ItemController]", text);
        return itemClient.searchItemsByNameAndDescription(from, size, text);
    }

    @DeleteMapping({"/{itemId}"})
    public ResponseEntity<Object> deleteItem(@PathVariable int itemId,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("deleteItem {}. Method [deleteItem] class [ItemController]", itemId);
        return itemClient.deleteItem(itemId, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDto commentDto,
                                                @PathVariable long itemId,
                                                @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("createComment {}. Method [createComment] class [ItemController]", commentDto);
        return itemClient.createComment(commentDto, itemId, userId);
    }
}
