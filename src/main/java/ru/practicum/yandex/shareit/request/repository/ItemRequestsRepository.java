package ru.practicum.yandex.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.yandex.shareit.item.model.Comment;
import ru.practicum.yandex.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestsRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findItemRequestsByRequestorIdOrderByCreatedDesc(long requestorId);
}
