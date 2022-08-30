package ru.practicum.yandex.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.yandex.shareit.user.model.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

}
