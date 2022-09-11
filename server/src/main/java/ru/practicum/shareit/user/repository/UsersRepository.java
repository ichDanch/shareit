package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;


public interface UsersRepository extends JpaRepository<User, Long> {

}
