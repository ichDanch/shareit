package ru.practicum.yandex.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.shareit.item.dto.CommentDto;
import ru.practicum.yandex.shareit.item.dto.ItemDto;
import ru.practicum.yandex.shareit.item.model.Comment;
import ru.practicum.yandex.shareit.item.model.Item;
@Component
public class CommentMapper {

    public CommentDto toDto(Comment comment) {

        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated())
                ;
    }

//    public Comment toComment(CommentDto commentDto) {
//        return new Comment(
//                commentDto.getId(),
//                commentDto.getText(),
//                commentDto.getCreated()
//        );
//    }

    public Comment toComment(CommentDto commentDto) {
        return new Comment(
                commentDto.getText()
        );
    }
}
