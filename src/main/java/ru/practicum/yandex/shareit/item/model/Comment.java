package ru.practicum.yandex.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.yandex.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "text")
    private String text;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @Column(name = "created")
    private LocalDateTime created;

    public Comment(String text) {
        this.text = text;
    }

    public Comment(long id, String text, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.created = created;
    }
}
