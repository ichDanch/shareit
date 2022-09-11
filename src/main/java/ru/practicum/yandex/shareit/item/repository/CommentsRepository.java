package ru.practicum.yandex.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.yandex.shareit.item.model.Comment;

import java.util.List;


public interface CommentsRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentByItemId(long itemId);
}
