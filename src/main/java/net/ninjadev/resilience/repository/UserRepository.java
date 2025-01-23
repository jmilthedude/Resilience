package net.ninjadev.resilience.repository;

import net.ninjadev.resilience.entity.ResilienceUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<ResilienceUser, Long> {
    Optional<ResilienceUser> findByUsername(String username);

    void deleteByUsername(String username);
}
