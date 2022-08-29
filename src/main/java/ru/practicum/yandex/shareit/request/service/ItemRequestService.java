package ru.practicum.yandex.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.yandex.shareit.exceptions.NotFoundException;
import ru.practicum.yandex.shareit.exceptions.ValidationException;
import ru.practicum.yandex.shareit.item.ItemMapper;
import ru.practicum.yandex.shareit.item.ItemsRepository;
import ru.practicum.yandex.shareit.item.dto.ItemDto;
import ru.practicum.yandex.shareit.item.model.Item;
import ru.practicum.yandex.shareit.request.model.ItemRequest;
import ru.practicum.yandex.shareit.request.ItemRequestMapper;
import ru.practicum.yandex.shareit.request.dto.ItemRequestDto;
import ru.practicum.yandex.shareit.request.repository.ItemRequestsRepository;
import ru.practicum.yandex.shareit.user.UsersRepository;
import ru.practicum.yandex.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemRequestService {
    private final ItemRequestsRepository itemRequestsRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final UsersRepository usersRepository;
    private final ItemsRepository itemsRepository;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemRequestService(ItemRequestsRepository itemRequestsRepository,
                              ItemRequestMapper itemRequestMapper,
                              UsersRepository usersRepository, ItemsRepository itemsRepository, ItemMapper itemMapper) {
        this.itemRequestsRepository = itemRequestsRepository;
        this.itemRequestMapper = itemRequestMapper;
        this.usersRepository = usersRepository;
        this.itemsRepository = itemsRepository;
        this.itemMapper = itemMapper;
    }

    public ItemRequestDto saveItemRequest(ItemRequestDto itemRequestDto, long userId) {
        User user = checkUser(userId);
        if (itemRequestDto.getDescription() == null) {
            throw new ValidationException("itemRequestDto description must not be null");
        }
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(user);
        ItemRequest itemRequestToReturn = itemRequestsRepository.save(itemRequest);
        return itemRequestMapper.toDto(itemRequestToReturn);
    }

    public List<ItemRequestDto> findRequestsByOwner(long userId) {
        User user = checkUser(userId);
        List<ItemRequest> requests = itemRequestsRepository.findItemRequestsByRequestorIdOrderByCreatedDesc(userId);
        List<Item> items = findItemsByRequests(requests);

        if (!requests.isEmpty() && !items.isEmpty()) {
            return setItemDtoToRequestDto(requests, items);
        } else {
            return itemRequestMapper.toDtos(requests);
        }
    }

    public ItemRequestDto findRequestById(long userId, long requestId) {
        User user = checkUser(userId);
        ItemRequest itemRequest = itemRequestsRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Does not contain itemRequest with this id or id is invalid " + userId));
        Item item = itemsRepository.findItemByItemRequestId(requestId).orElseThrow(() ->
                new NotFoundException("Does not contain item with this id or id is invalid " + userId));
        ItemDto itemDto = itemMapper.toDto(item);
        ItemRequestDto itemRequestDto = itemRequestMapper.toDto(itemRequest);
        itemRequestDto.getItems().add(itemDto);

        return itemRequestDto;
    }

    private User checkUser(long userId) {
        return usersRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Does not contain user with this id or id is invalid " + userId));
    }

    public List<ItemRequestDto> findAllRequests(int from, int size, long userId) {
        if (from < 0 || size<=0) {
            throw new ValidationException("from or size are not valid");
        }
        User user = checkUser(userId);

        Pageable pageWithElements = PageRequest.of(from / size, size, Sort.by("created").descending());

        Page<ItemRequest> page = itemRequestsRepository.findAllByRequestorIsNot(user, pageWithElements);

        List<ItemRequest> itemRequests = page.get().collect(Collectors.toList());

        List<Item> items = findItemsByRequests(itemRequests);

        return setItemDtoToRequestDto(itemRequests, items);
    }


    private List<ItemRequestDto> setItemDtoToRequestDto(List<ItemRequest> requests, List<Item> items) {

        List<ItemRequestDto> itemRequestsDto = new ArrayList<>();

        for (ItemRequest itemRequest : requests) {
            for (Item item : items) {
                if (item.getItemRequest() != null) {
                    if (itemRequest.getId() == item.getItemRequest().getId()) {
                        ItemDto itemDto = itemMapper.toDto(item);
                        ItemRequestDto itemRequestDto = itemRequestMapper.toDto(itemRequest);
                        itemRequestDto.getItems().add(itemDto);
                        itemRequestsDto.add(itemRequestDto);
                    }
                }
            }
        }
        return itemRequestsDto;
    }

    private List<Item> findItemsByRequests(List<ItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(ItemRequest::getId)
                .map(itemsRepository::findItemByItemRequestId)
                .flatMap(Optional::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
