package ru.practicum.yandex.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.yandex.shareit.user.model.User;


public interface UsersRepository extends JpaRepository<User, Long> {

}
