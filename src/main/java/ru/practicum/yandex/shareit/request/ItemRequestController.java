package ru.practicum.yandex.shareit.request;

import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.shareit.request.dto.ItemRequestDto;
import ru.practicum.yandex.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestDto createItemRequest(@Valid @NotNull @RequestBody ItemRequestDto itemRequestDto,
                                            @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.saveItemRequest(itemRequestDto, userId);
    }

    @GetMapping()
    List<ItemRequestDto> findRequestsByOwner(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId) {

        return itemRequestService.findRequestsByOwner(userId);
    }

    @GetMapping("/{requestId}")
    ItemRequestDto findRequestById(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                                   @NotNull @PathVariable long requestId) {

        return itemRequestService.findRequestById(userId, requestId);
    }

    @GetMapping("/all")
    List<ItemRequestDto> findAllRequests(@RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "20") int size,
                                         @NotBlank @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.findAllRequests(from, size, userId);
    }
}
