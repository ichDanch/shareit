package ru.practicum.yandex.shareit.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.practicum.yandex.shareit.request.dto.ItemRequestDto;
import ru.practicum.yandex.shareit.request.model.ItemRequest;

import javax.persistence.ManyToOne;
import java.util.List;
@Mapper(componentModel = "spring")
@Component
public interface ItemRequestMapper {

    ItemRequestMapper INSTANCE = Mappers.getMapper(ItemRequestMapper.class);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "created", target = "created")
    ItemRequestDto toDto(ItemRequest itemRequest);
   // @Mapping(target = "requestor", expression = "java(itemRequest.setRequestor(user))")
   @Mapping(target = "id", ignore = true)
   @Mapping(target = "created", ignore = true)
   //@Mapping(target = "items", ignore = true)
    ItemRequest toItemRequest (ItemRequestDto itemRequestDto);

    List<ItemRequestDto> toDtos(List<ItemRequest> itemRequests);

/*        @Mapping(source = "product.desc", target = "description", defaultValue = "description")
//@Mapping(source = "product.items", target = "itemsList")
        ProductDto modelToDto(Product product);

        List<ProductDto> modelToDtos(List<Product> product);

        @InheritInverseConfiguration
        Product dtoToModel(ProductDto productDto);*/

}
