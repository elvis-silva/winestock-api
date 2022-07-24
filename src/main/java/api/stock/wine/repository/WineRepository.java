package api.stock.wine.repository;

import api.stock.wine.entity.Wine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WineRepository extends JpaRepository<Wine, Long> {

    Optional<Wine> findByName(String wineName);
}
