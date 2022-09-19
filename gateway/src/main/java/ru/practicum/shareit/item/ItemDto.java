package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoToItem;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor()
@NoArgsConstructor
@Builder
public class ItemDto {
    private long id;
    @NotEmpty (message = "Name cannot be empty")
    private String name;
    @NotEmpty (message = "Description cannot be empty")
    private String description;
    @NotNull
    private Boolean available;
    private BookingDtoToItem nextBooking;
    private BookingDtoToItem lastBooking;
    private List<CommentDto> comments;
    private long requestId;
}
