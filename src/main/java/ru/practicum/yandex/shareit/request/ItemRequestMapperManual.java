/*
package ru.practicum.yandex.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.shareit.request.dto.ItemRequestDto;
import ru.practicum.yandex.shareit.request.model.ItemRequest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class ItemRequestMapperManual {

    public ItemRequestDto toDto(ItemRequest itemRequest) {
        if ( itemRequest == null ) {
            return null;
        }

        long id = 0L;
        Instant created = null;
        String description = null;

        id = itemRequest.getId();
        created = itemRequest.getCreated();
        description = itemRequest.getDescription();

        ItemRequestDto itemRequestDto = new ItemRequestDto( id, description, created);

        return itemRequestDto;
    }

    @Override
    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        if ( itemRequestDto == null ) {
            return null;
        }

        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setDescription( itemRequestDto.getDescription() );

        return itemRequest;
    }

    @Override
    public List<ItemRequestDto> toDtos(List<ItemRequest> itemRequests) {
        if ( itemRequests == null ) {
            return null;
        }

        List<ItemRequestDto> list = new ArrayList<ItemRequestDto>( itemRequests.size() );
        for ( ItemRequest itemRequest : itemRequests ) {
            list.add( toDto( itemRequest ) );
        }

        return list;
    }
}
*/
