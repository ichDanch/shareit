package ru.practicum.yandex.shareit.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.practicum.yandex.shareit.request.dto.ItemRequestDto;
import ru.practicum.yandex.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring",imports = {ArrayList.class})
@Component
public interface ItemRequestMapper {

    ItemRequestMapper INSTANCE = Mappers.getMapper(ItemRequestMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "created", target = "created")
    ItemRequestDto toDto(ItemRequest itemRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    ItemRequest toItemRequest(ItemRequestDto itemRequestDto);

    List<ItemRequestDto> toDtos(List<ItemRequest> itemRequests);
}
