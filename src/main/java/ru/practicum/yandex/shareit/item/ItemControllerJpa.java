package ru.practicum.yandex.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.shareit.exceptions.UserNotFoundException;
import ru.practicum.yandex.shareit.exceptions.ValidationException;
import ru.practicum.yandex.shareit.item.dto.ItemDto;
import ru.practicum.yandex.shareit.item.model.Item;
import ru.practicum.yandex.shareit.item.service.ItemServiceJpa;
import ru.practicum.yandex.shareit.user.service.UserServiceJpa;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class ItemControllerJpa {
    private ItemServiceJpa itemServiceJpa;
    private UserServiceJpa userServiceJpa;
    private ItemMapper itemMapper;

    @Autowired
    public ItemControllerJpa(ItemServiceJpa itemServiceJpa, UserServiceJpa userServiceJpa, ItemMapper itemMapper) {
        this.itemServiceJpa = itemServiceJpa;
        this.userServiceJpa = userServiceJpa;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    public ItemDto create(@Valid @NotNull @RequestBody ItemDto itemDto,  // Приходит имя, описание, статус
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        // 1. перенести логику в сервис
        // 2. проверку вынести в отделньый метод
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("itemDto available must not be null");
        }
        if (itemDto.getName() == null || itemDto.getName().equals(" ") || itemDto.getName().equals("")) {
            throw new ValidationException("itemDto name must not be null or blank or empty");
        }
        if (itemDto.getDescription() == null) {
            throw new ValidationException("itemDto description must not be null");
        }

        userServiceJpa.findById(userId);  //проверяем наличие пользователя
        Item toItem = itemMapper.toItem(itemDto);
        toItem.setOwner(userId);
        Item item = itemServiceJpa.save(toItem);
        return itemMapper.toDto(item);   // вернуть айди, имя, описание, стастус
    }

    @PatchMapping({"/{itemId}"})
    public ItemDto patch(@Valid @RequestBody ItemDto itemDto,
                         @PathVariable long itemId,
                         @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {
        // 1. перенести логику в сервис
        // 2. проверку вынести в отделньый метод

        Item item = itemServiceJpa.findById(itemId);// тут уже есть проверка на налчиие в базе
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
        Item patchedItem = itemServiceJpa.save(item);
        return itemMapper.toDto(patchedItem);
    }

    @GetMapping({"/{id}"})
    public ItemDto get(@PositiveOrZero @PathVariable int id,
                       @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {
        Item item = itemServiceJpa.findById(id);
        return itemMapper.toDto(item);
    }

    @GetMapping
    public List<ItemDto> findAllUserItem(@PositiveOrZero @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemServiceJpa.findAllUserItem(ownerId)
                .stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping({"/search"})
    public List<ItemDto> itemsByNameAndDescription(@RequestParam String text) {
        return itemServiceJpa.itemsByNameAndDescription(text)
                .stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());

    }

    @DeleteMapping({"/{id}"})
    public void deleteItem(@PositiveOrZero @PathVariable int id,
                           @PositiveOrZero @RequestHeader("X-Sharer-User-Id") long userId) {
        Item item = itemServiceJpa.findById(id);
        checkOwner(userId, item);
        itemServiceJpa.deleteItemById(id);
    }

    private void checkOwner(long userId, Item item) {
        if (item.getOwner() != userId) {
            throw new UserNotFoundException("Only the owner can change item");
        }
    }
}
