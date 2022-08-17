package ru.practicum.yandex.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.shareit.exceptions.NotFoundException;
import ru.practicum.yandex.shareit.item.dto.ItemDto;
import ru.practicum.yandex.shareit.item.model.Item;
import ru.practicum.yandex.shareit.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemController(ItemService itemService,
                          UserService userService,
                          ItemMapper itemMapper) {
        this.itemService = itemService;
        this.userService = userService;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    public ItemDto createItem(@Valid @NotNull @RequestBody ItemDto itemDto,  // Приходит имя, описание, статус
                              @RequestHeader("X-Sharer-User-Id") long userId) {

        return itemService.saveItem(itemDto, userId);

    }

    @PatchMapping({"/{itemId}"})
    public ItemDto patchItem(@Valid @RequestBody ItemDto itemDto,
                             @PathVariable long itemId,
                             @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {
      return itemService.patchItem(itemDto, itemId, userId);
    }

    @GetMapping({"/{id}"})
    public ItemDto findItemById(@PositiveOrZero @PathVariable int id,
                                @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {
        Item item = itemService.findById(id);
        return itemMapper.toDto(item);
    }

    @GetMapping
    public List<ItemDto> findAllOwnersItems(@PositiveOrZero @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.findAllOwnersItems(ownerId)
                .stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping({"/search"})
    public List<ItemDto> itemsByNameAndDescription(@RequestParam String text) {
        return itemService.itemsByNameAndDescription(text)
                .stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());

    }

    @DeleteMapping({"/{id}"})
    public void deleteItem(@PositiveOrZero @PathVariable int id,
                           @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {
        Item item = itemService.findById(id);
        checkOwner(userId, item);
        itemService.deleteItemById(id);
    }

    private void checkOwner(long userId, Item item) {
        if (item.getOwner().getId() != userId) {
            throw new NotFoundException("Only the owner can change item");
        }
    }
}
