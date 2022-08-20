package ru.practicum.yandex.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.shareit.item.dto.CommentDto;
import ru.practicum.yandex.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemController(ItemService itemService,
                          ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    public ItemDto createItem(@Valid @NotNull @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") long userId) {

        return itemService.saveItem(itemDto, userId);

    }

    @PatchMapping({"/{itemId}"})
    public ItemDto patchItem(@Valid @RequestBody ItemDto itemDto,
                             @PathVariable long itemId,
                             @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.patchItem(itemDto, itemId, userId);
    }

    @GetMapping({"/{itemId}"})
    public ItemDto findItemById(@PositiveOrZero @PathVariable int itemId,
                                @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findById(itemId, userId);

    }

    @GetMapping
    public List<ItemDto> findAllItemsByOwnerId(@PositiveOrZero @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.findAllItemsByOwnerId(ownerId);
    }

    @GetMapping({"/search"})
    public List<ItemDto> itemsByNameAndDescription(@RequestParam String text) {
        return itemService.itemsByNameAndDescription(text)
                .stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());

    }

    @DeleteMapping({"/{itemId}"})
    public void deleteItem(@PositiveOrZero @PathVariable int itemId,
                           @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {

        itemService.deleteItemById(itemId, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@Valid @NotNull @RequestBody CommentDto commentDto,
                                    @PathVariable long itemId,
                                    @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.createCommentByUser(commentDto, itemId, userId);

    }
}
