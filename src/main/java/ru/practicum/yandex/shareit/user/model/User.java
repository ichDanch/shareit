package ru.practicum.yandex.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
@AllArgsConstructor
public class User {
    @PositiveOrZero
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @NotEmpty
    @NotBlank(message = "Name cannot be null or empty")
    @Column(name = "name")
    private String name;
    @Email(message = "Email should be valid")
    @Column(name = "email")
    private String email;



    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
