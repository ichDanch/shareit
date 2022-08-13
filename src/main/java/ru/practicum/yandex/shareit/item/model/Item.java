package ru.practicum.yandex.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.yandex.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "items")
public class Item {
    @PositiveOrZero
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @NotEmpty
    @NotBlank(message = "Name cannot be null or empty")
    @Column (name = "name")
    private String name;
    @Size(max = 200, message = "Description must be less then 200 characters")
    @Column(name = "description")
    private String description;
    @NotNull
    @Column(name = "available")
    private Boolean available; // Статус должен проставлять владелец
    @Column(name = "owner_id")
    private long owner; // владелец вещи
   /* @ElementCollection
    @CollectionTable(name="requests", joinColumns=@JoinColumn(name="item_id"))
    @Column(name="name")*/

    //@OneToOne
    //@JoinColumn(name ="id", referencedColumnName = "id")
   // private ItemRequest request; // если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос.

    public Item(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }
public Item(String name, String description, Boolean available, long owner) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }

}
