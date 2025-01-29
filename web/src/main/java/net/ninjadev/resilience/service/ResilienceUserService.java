package net.ninjadev.resilience.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import net.ninjadev.resilience.dto.ResilienceUserDTO;
import net.ninjadev.resilience.entity.ResilienceUser;
import net.ninjadev.resilience.repository.ResilienceUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResilienceUserService {

    private final ResilienceUserRepository resilienceUserRepository;
    private final PasswordEncoder passwordEncoder;

    public ResilienceUserService(ResilienceUserRepository resilienceUserRepository, PasswordEncoder passwordEncoder) {
        this.resilienceUserRepository = resilienceUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<ResilienceUser> findById(String id) {
        return this.resilienceUserRepository.findById(Long.parseLong(id));
    }

    public Optional<ResilienceUser> add(@Valid ResilienceUser user) {
        if (this.resilienceUserRepository.findByUsername(user.getUsername()).isPresent()) {
            return Optional.empty();
        }

        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);  // Set the encoded password

        this.resilienceUserRepository.save(user);
        return Optional.of(user);
    }

    public Optional<ResilienceUser> add(@Valid ResilienceUserDTO user) {
        return this.add(new ResilienceUser(user));
    }

    @Transactional
    public boolean deleteByUsername(String username) {
        if (this.resilienceUserRepository.findByUsername(username).isEmpty()) {
            return false;
        }
        this.resilienceUserRepository.deleteByUsername(username);
        return true;
    }
}
