package ru.practicum.yandex.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.yandex.shareit.exceptions.NotFoundException;
import ru.practicum.yandex.shareit.exceptions.ValidationException;
import ru.practicum.yandex.shareit.item.dto.ItemDto;
import ru.practicum.yandex.shareit.item.model.Item;
import ru.practicum.yandex.shareit.user.UserService;
import ru.practicum.yandex.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ItemService {
    private final ItemsRepository itemsRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemService(ItemsRepository itemsRepository, UserService userService, ItemMapper itemMapper) {
        this.itemsRepository = itemsRepository;
        this.userService = userService;
        this.itemMapper = itemMapper;
    }

    @Transactional
    public ItemDto saveItem(ItemDto itemDto, long userId) {
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

        User owner = userService.findById(userId);  //проверяем наличие пользователя и получаем владельца
        Item toItem = itemMapper.toItem(itemDto);   //преобразуем дто в объект
        toItem.setOwner(owner);                     //присваиваем вещи владельца
        Item item = itemsRepository.save(toItem);   //сохраняем объект в базу и возвращаем вещь с присвоенным айди
        return itemMapper.toDto(item);              //возвращаем дто
    }
    @Transactional
    public ItemDto patchItem(ItemDto itemDto, long itemId, long userId) {
        Item item = findById(itemId);                   // проверяем наличие вещи в базе

        if (item.getOwner() == null || item.getOwner().getId() != userId) {        // проверяем владельца
            throw new NotFoundException("Only the owner can change item");
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        System.out.println("До пропатченая вещь: ");

        Item patchedItem = itemsRepository.save(item);

        System.out.println("Пропатченая вещь: "+patchedItem);

        return itemMapper.toDto(patchedItem);
    }

    public List<Item> findAllOwnersItems(Long ownerId) {
        return itemsRepository.findAll()
                .stream()
                .filter(u -> u.getOwner().getId() == ownerId)
                .collect(Collectors.toList());
    }

    public Item findById(long id) {
        return itemsRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Does not contain item with this id or id is invalid " + id));
    }

    @Transactional
    public void deleteItemById(long id) {
        findById(id);
        itemsRepository.deleteById(id);
    }

    public List<Item> findAllItems() {
        return itemsRepository.findAll();
    }

    public List<Item> itemsByNameAndDescription(String text) {

        //1. возвращает только доступные для аренды вещи
        //2. искать текст в названии и описании
        if (text.isBlank()) {
            return new ArrayList<Item>();
        }
        // написать SQL запрос вместо этого
        return findAllItems()
                .stream()
                .filter(u -> u.getAvailable() == true)
                .filter(u ->
                        u.getName().toLowerCase().contains(text.toLowerCase())
                                || u.getDescription().toLowerCase().contains((text.toLowerCase())))
                .collect(Collectors.toList());
    }


    private void checkOwner(long userId, Item item) {
        if (item.getOwner().getId() != userId) {
            throw new NotFoundException("Only the owner can change item");
        }
    }

}
