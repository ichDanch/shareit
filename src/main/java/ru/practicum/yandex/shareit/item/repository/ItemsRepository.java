package ru.practicum.yandex.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.yandex.shareit.item.model.Item;
import ru.practicum.yandex.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ItemsRepository extends JpaRepository<Item, Long> {
    Optional<Item> findItemByItemRequestId(long itemRequestId);

    Page<Item> findItemsByOwner(User owner, Pageable pageable);

    List<Item> findItemsByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(String name, String description);

    Page<Item> findItemsByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
            String name,
            String description,
            Pageable pageable);


}
