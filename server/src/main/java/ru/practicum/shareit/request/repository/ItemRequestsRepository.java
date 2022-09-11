package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;


public interface ItemRequestsRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findItemRequestsByRequestorIdOrderByCreatedDesc(long requestorId);

    Page<ItemRequest> findAllByRequestorIsNot(User user, Pageable pageable);
}
