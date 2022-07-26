package ru.practicum.shareit.request;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestServiceImpl itemRequestServiceImpl;

    public ItemRequestController(ItemRequestServiceImpl itemRequestServiceImpl) {
        this.itemRequestServiceImpl = itemRequestServiceImpl;
    }

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestBody ItemRequestDto itemRequestDto,
                                            @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestServiceImpl.saveItemRequest(itemRequestDto, userId);
    }

    @GetMapping()
    List<ItemRequestDto> findRequestsByOwner(@RequestHeader("X-Sharer-User-Id") long userId) {

        return itemRequestServiceImpl.findRequestsByOwner(userId);
    }

    @GetMapping("/{requestId}")
    ItemRequestDto findRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @PathVariable long requestId) {

        return itemRequestServiceImpl.findRequestById(userId, requestId);
    }

    @GetMapping("/all")
    List<ItemRequestDto> findAllRequests(@RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "20") int size,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestServiceImpl.findAllRequests(from, size, userId);
    }
}
