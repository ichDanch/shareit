package ru.practicum.yandex.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.yandex.shareit.item.model.Item;
import ru.practicum.yandex.shareit.request.model.ItemRequest;
import ru.practicum.yandex.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemsRepository extends JpaRepository<Item, Long> {
    List<Item> findItemsByOwnerId(long ownerId);

    Optional<Item> findItemByItemRequestId (long itemRequestId);
    Page <Item> findItemsByOwner(User owner, Pageable pageable);
    //Page<ItemRequest> findAllByRequestorIsNot(User user, Pageable pageable);


}
