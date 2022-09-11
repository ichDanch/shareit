package ru.practicum.shareit.request;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestServiceImpl itemRequestServiceImpl;

    public ItemRequestController(ItemRequestServiceImpl itemRequestServiceImpl) {
        this.itemRequestServiceImpl = itemRequestServiceImpl;
    }

    @PostMapping
    public ItemRequestDto createItemRequest(@Valid @NotNull @RequestBody ItemRequestDto itemRequestDto,
                                            @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestServiceImpl.saveItemRequest(itemRequestDto, userId);
    }

    @GetMapping()
    List<ItemRequestDto> findRequestsByOwner(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId) {

        return itemRequestServiceImpl.findRequestsByOwner(userId);
    }

    @GetMapping("/{requestId}")
    ItemRequestDto findRequestById(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                                   @NotNull @PathVariable long requestId) {

        return itemRequestServiceImpl.findRequestById(userId, requestId);
    }

    @GetMapping("/all")
    List<ItemRequestDto> findAllRequests(@RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "20") int size,
                                         @NotBlank @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestServiceImpl.findAllRequests(from, size, userId);
    }
}
