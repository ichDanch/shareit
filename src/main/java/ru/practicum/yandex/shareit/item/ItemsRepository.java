package ru.practicum.yandex.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.yandex.shareit.item.model.Item;

@Repository
public interface ItemsRepository extends JpaRepository<Item, Long> {
}
