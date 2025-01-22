package net.ninjadev.resilience.repository;

import net.ninjadev.resilience.entity.ResilienceUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<ResilienceUser, Long> {
    ResilienceUser findByUsername(String username);
}
