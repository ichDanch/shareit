package ru.practicum.yandex.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.shareit.exceptions.UserNotFoundException;
import ru.practicum.yandex.shareit.exceptions.ValidationException;
import ru.practicum.yandex.shareit.item.dto.ItemDto;
import ru.practicum.yandex.shareit.item.model.Item;
import ru.practicum.yandex.shareit.item.service.ItemService;
import ru.practicum.yandex.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

    private ItemService itemService;
    private UserService userService;
    private ItemMapper itemMapper;

    @Autowired
    public ItemController(ItemService itemService, UserService userService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.userService = userService;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    public ItemDto create(@Valid @NotNull @RequestBody ItemDto itemDto,  // Приходит имя, описание, статус
                          @RequestHeader("X-Sharer-User-Id") long userId) {

        if (itemDto.getAvailable() == null) {
            throw new ValidationException("itemDto available must not be null");
        }
        if (itemDto.getName() == null || itemDto.getName().equals(" ") || itemDto.getName().equals("")) {
            throw new ValidationException("itemDto name must not be null or blank or empty");
        }
        if (itemDto.getDescription() == null) {
            throw new ValidationException("itemDto description must not be null");
        }

        userService.get(userId);  //проверяем наличие пользователя
        Item toItem = itemMapper.toItem(itemDto);
        toItem.setOwner(userId);
        Item item = itemService.create(toItem);
        return itemMapper.toDto(item);   // вернуть айди, имя, описание, стастус
    }

    @PatchMapping({"/{itemId}"})
    public ItemDto patch(@Valid @RequestBody ItemDto itemDto,
                         @PathVariable long itemId,
                         @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {

        Item item = itemService.get(itemId);// тут уже есть проверка на налчиие в базе
        checkOwner(userId, item);
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        Item patchedItem = itemService.patch(item);
        return itemMapper.toDto(patchedItem);
    }


    @GetMapping
    public List<ItemDto> getAllItem(@PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllItems()
                .stream()
                .filter(u -> u.getOwner() == userId)
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping({"/{id}"})
    public ItemDto get(@PositiveOrZero @PathVariable int id,
                       @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {
        Item item = itemService.get(id);
        return itemMapper.toDto(item);
    }

    @DeleteMapping({"/{id}"})
    public void deleteItem(@PositiveOrZero @PathVariable int id,
                           @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {
        Item item = itemService.get(id);
        checkOwner(userId, item);
        itemService.delete(id);
    }

    @GetMapping({"/search"})
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text)
                .stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());

    }

    private void checkOwner(long userId, Item item) {
        if (item.getOwner() != userId) {
            throw new UserNotFoundException("Only the owner can change item");
        }
    }

}
