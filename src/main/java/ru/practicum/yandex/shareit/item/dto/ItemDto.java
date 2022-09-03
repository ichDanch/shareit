package ru.practicum.yandex.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.yandex.shareit.booking.dto.BookingDtoToItem;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor()
@NoArgsConstructor
@Builder
public class ItemDto {
    private long id;
    private String name;
    @Size(max = 200, message = "Description must be less then 200 characters")
    private String description;
    private Boolean available;
    private BookingDtoToItem nextBooking;
    private BookingDtoToItem lastBooking;
    private List<CommentDto> comments;
    private long requestId;

    public ItemDto(long id, String name, String description, Boolean available, long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }

    public ItemDto(long id, String name, String description, Boolean available, long requestId, List<CommentDto> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
        this.comments = comments;
    }
}
