package ru.practicum.yandex.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.yandex.shareit.exceptions.ItemNotFoundException;
import ru.practicum.yandex.shareit.item.ItemsRepository;
import ru.practicum.yandex.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ItemServiceJpa {
    private final ItemsRepository itemsRepository;

    @Autowired
    public ItemServiceJpa(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Transactional
    public Item save(Item item) {
        return itemsRepository.save(item);
    }

    public List<Item> findAllUserItem(Long ownerId) {
        return itemsRepository.findAll()
                .stream()
                .filter(u -> u.getOwner() == ownerId)
                .collect(Collectors.toList());
    }

    public Item findById(long id) {
        return itemsRepository.findById(id)
                .orElseThrow(() ->
                        new ItemNotFoundException("Does not contain item with this id or id is invalid " + id));
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

}
