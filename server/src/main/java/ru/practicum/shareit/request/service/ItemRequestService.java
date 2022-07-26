package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto saveItemRequest(ItemRequestDto itemRequestDto, long userId);

    List<ItemRequestDto> findRequestsByOwner(long userId);

    ItemRequestDto findRequestById(long userId, long requestId);

    List<ItemRequestDto> findAllRequests(int from, int size, long userId);
}
