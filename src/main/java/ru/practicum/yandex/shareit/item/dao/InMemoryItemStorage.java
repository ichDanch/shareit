package ru.practicum.yandex.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.shareit.item.model.Item;
import ru.practicum.yandex.shareit.user.model.User;

import java.util.*;

@Component
public class InMemoryItemStorage implements ItemStorage {
    private Map<Long, Item> items = new HashMap<>();
    private long ITEM_COUNT;

    @Override
    public Item create(Item item) {
        item.setId(++ITEM_COUNT);
        items.put(item.getId(),item);
        return item;
    }

    @Override
    public Item patch(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Optional<Item> getItem(long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public void delete(long id) {
        items.remove(id);
    }
}
