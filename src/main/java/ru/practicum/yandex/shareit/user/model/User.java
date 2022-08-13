package ru.practicum.yandex.shareit.user.model;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@Entity
@NoArgsConstructor
@Table (name = "users")
@AllArgsConstructor
public class User {
    @PositiveOrZero
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @NotEmpty
    @NotBlank(message = "Name cannot be null or empty")
    @Column (name = "name")
    private String name;
    @Email(message = "Email should be valid")
    @Column (name = "email")
    private String email;
   // @OneToMany (mappedBy = "owner")
   // private List<Item> items;


}
