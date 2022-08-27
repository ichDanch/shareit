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
    /**
     * POST /requests — добавить новый запрос вещи. Основная часть запроса — текст запроса, где пользователь описывает,
     * какая именно вещь ему нужна.
     * GET /requests — получить список своих запросов вместе с данными об ответах на них.
     * Для каждого запроса должны указываться:
     * -описание
     * -дата
     * -время создания
     * -список ответов в формате: id вещи, название, id владельца.
     * Так в дальнейшем, используя указанные id вещей, можно будет
     * получить подробную информацию о каждой вещи. Запросы должны возвращаться в отсортированном порядке от более новых
     * к более старым.
     * GET /requests/all?from={from}&size={size} — получить список запросов, созданных другими пользователями.
     * С помощью этого эндпоинта пользователи смогут просматривать существующие запросы, на которые они могли бы ответить.
     * Запросы сортируются по дате создания: от более новых к более старым. Результаты должны возвращаться постранично.
     * Для этого нужно передать два параметра: from — индекс первого элемента, начиная с 0, и size — количество
     * элементов для отображения.
     * GET /requests/{requestId} — получить данные об одном конкретном запросе вместе с данными об ответах на него
     * в том же формате, что и в эндпоинте GET /requests. Посмотреть данные об отдельном запросе может любой пользователь.
     */
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

}
