package ru.estartsev.rpstg.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.estartsev.rpstg.entity.Player;

import java.util.Optional;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {
    Player findById(long id);

    Optional<Player> findByUserId(Long userId);
}