package ru.practicum.yandex.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.yandex.shareit.item.model.Comment;
import ru.practicum.yandex.shareit.item.model.Item;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long> {
        List<Comment> findCommentByItemId(long itemId);
}
