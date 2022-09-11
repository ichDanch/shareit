package ru.practicum.yandex.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.shareit.item.dto.CommentDto;
import ru.practicum.yandex.shareit.item.dto.ItemDto;
import ru.practicum.yandex.shareit.item.mapper.ItemMapper;
import ru.practicum.yandex.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemServiceImpl itemServiceImpl;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemController(ItemServiceImpl itemServiceImpl,
                          ItemMapper itemMapper) {
        this.itemServiceImpl = itemServiceImpl;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    public ItemDto createItem(@Valid @NotNull @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") long userId) {

        return itemServiceImpl.saveItem(itemDto, userId);

    }

    @PatchMapping({"/{itemId}"})
    public ItemDto patchItem(@Valid @RequestBody ItemDto itemDto,
                             @PathVariable long itemId,
                             @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemServiceImpl.patchItem(itemDto, itemId, userId);
    }

    @GetMapping({"/{itemId}"})
    public ItemDto findItemById(@PositiveOrZero @PathVariable int itemId,
                                @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemServiceImpl.findById(itemId, userId);

    }

    @GetMapping
    public List<ItemDto> findAllItemsByOwnerId(@RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "20") int size,
                                               @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemServiceImpl.findAllItemsByOwnerId(from, size, ownerId);
    }

    @GetMapping({"/search"})
    public List<ItemDto> itemsByNameAndDescription(@RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "20") int size,
                                                   @RequestParam String text) {
        return itemServiceImpl.itemsByNameAndDescription(from, size, text);
    }

    @DeleteMapping({"/{itemId}"})
    public void deleteItem(@PositiveOrZero @PathVariable int itemId,
                           @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {

        itemServiceImpl.deleteItemById(itemId, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@Valid @NotNull @RequestBody CommentDto commentDto,
                                    @PathVariable long itemId,
                                    @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemServiceImpl.createCommentByUser(commentDto, itemId, userId);

    }
}
