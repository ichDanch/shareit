package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoToItem;

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
}
