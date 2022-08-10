package ru.practicum.yandex.shareit.item.service;

import ru.practicum.yandex.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(Item item);
    Item patchItem(Item item);
    List<Item> getAllItems();
    Item getItemById(long id);
    void deleteItemById(long id);
    List<Item> itemsByNameAndDescription(String text);
}
