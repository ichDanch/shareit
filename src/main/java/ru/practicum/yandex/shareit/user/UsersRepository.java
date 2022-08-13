package ru.practicum.yandex.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.yandex.shareit.user.model.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
}
