package ru.practicum.shareit.item.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    @Transactional
    ItemDto saveItem(ItemDto itemDto, long userId);

    @Transactional
    ItemDto patchItem(ItemDto itemDto, long itemId, long userId);

    List<ItemDto> findAllItemsByOwnerId(int from, int size, Long ownerId);

    ItemDto findById(long itemId, long userId);

    @Transactional
    void deleteItemById(long itemId, long userId);

    List<ItemDto> itemsByNameAndDescription(int from, int size, String text);

    @Transactional
    CommentDto createCommentByUser(CommentDto commentDto, long itemId, long userId);
}
