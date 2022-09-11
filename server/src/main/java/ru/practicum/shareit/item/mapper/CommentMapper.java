package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

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

    public Comment toComment(CommentDto commentDto) {
        return new Comment(
                commentDto.getText()
        );
    }
}
