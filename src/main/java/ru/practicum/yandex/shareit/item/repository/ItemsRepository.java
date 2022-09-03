package ru.practicum.yandex.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.yandex.shareit.item.model.Item;
import ru.practicum.yandex.shareit.user.model.User;

import java.util.Optional;

public interface ItemsRepository extends JpaRepository<Item, Long> {
    Optional<Item> findItemByItemRequestId(long itemRequestId);

    Page<Item> findItemsByOwner(User owner, Pageable pageable);

    Page<Item> findItemsByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
            String name,
            String description,
            Pageable pageable);


}
