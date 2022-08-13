/*
package ru.practicum.yandex.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.yandex.shareit.exceptions.ItemNotFoundException;
import ru.practicum.yandex.shareit.exceptions.UserNotFoundException;
import ru.practicum.yandex.shareit.item.ItemMapper;
import ru.practicum.yandex.shareit.item.dao.ItemStorage;
import ru.practicum.yandex.shareit.item.dto.ItemDto;
import ru.practicum.yandex.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService{
    private ItemStorage itemStorage;
    private ItemMapper itemMapper;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, ItemMapper itemMapper) {
        this.itemStorage = itemStorage;
        this.itemMapper = itemMapper;
    }

    @Override
    public Item create(Item item) {
        return itemStorage.create(item);
    }

    @Override
    public Item patch(Item item) {
        get(item.getId());
        return itemStorage.patch(item);
    }

    @Override
    public List<Item> getAllItems() {
        return itemStorage.getAllItems();
    }

    @Override
    public Item get(long id) {
        return itemStorage.getItem(id)
                .orElseThrow(() ->
                        new ItemNotFoundException("Does not contain item with this id or id is invalid " + id));
    }

    @Override
    public void delete(long id) {
        itemStorage.delete(id);
    }

    @Override
    public List<Item> search(String text) {

            //1. возвращает только доступные для аренды вещи
            //2. искать текст в названии и описании
            if (text.isBlank()) {
                return new ArrayList<Item>();
            }

            return  getAllItems()
                    .stream()
                    .filter(u -> u.getAvailable() == true)
                    .filter(u ->
                            u.getName().toLowerCase().contains(text.toLowerCase())
                                    || u.getDescription().toLowerCase().contains((text.toLowerCase())))
                    .collect(Collectors.toList());

    }
}
*/
