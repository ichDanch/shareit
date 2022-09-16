package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                                    @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("createItemRequest {}. Method [createItemRequest] class [ItemRequestController]", itemRequestDto);
        return itemRequestClient.createItemRequest(itemRequestDto, userId);
    }

    @GetMapping()
    public ResponseEntity<Object> findRequestsByOwnerId(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.info(" findRequestsByOwner {}. Method [findRequestsByOwner] class [ItemRequestController]", ownerId);
        return itemRequestClient.findRequestsByOwnerId(ownerId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @PathVariable long requestId) {
        log.info(" findRequestById {}. Method [findRequestById] class [ItemRequestController]", requestId);
        return itemRequestClient.findRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllRequests(@Valid @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "20") int size,
                                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("findAllRequests. Method [findAllRequests] class [ItemRequestController]");
        return itemRequestClient.findAllRequests(from, size, userId);
    }
}
