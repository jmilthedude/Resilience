package net.ninjadev.resilience.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ninjadev.resilience.config.ResilienceConfiguration;
import net.ninjadev.resilience.entity.ResilienceUser;
import net.ninjadev.resilience.repository.ResilienceUserRepository;
import net.ninjadev.resilience.request.AddUserRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResilienceUserService {

    private final ResilienceUserRepository resilienceUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResilienceConfiguration configuration;

    public Optional<ResilienceUser> findByUsername(String username) {
        return this.resilienceUserRepository.findByUsername(username);
    }

    public Optional<ResilienceUser> findById(String id) {
        return this.resilienceUserRepository.findById(Long.parseLong(id));
    }

    public List<ResilienceUser> getAllUsers() {
        return this.resilienceUserRepository.findAll();
    }

    public Optional<ResilienceUser> add(@Valid ResilienceUser user) {
        if (this.resilienceUserRepository.findByUsername(user.getUsername()).isPresent()) {
            return Optional.empty();
        }

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        this.resilienceUserRepository.save(user);
        return Optional.of(user);
    }

    public Optional<ResilienceUser> add(@Valid AddUserRequest user) {
        return this.add(new ResilienceUser(user, configuration));
    }

    @Transactional
    public boolean deleteById(String id) {
        log.info("Deleting user with id: {}", id);
        this.resilienceUserRepository.deleteById(Long.parseLong(id));
        return true;
    }

    public boolean updatePassword(String username, String oldPassword, String newPassword) {
        Optional<ResilienceUser> user = this.resilienceUserRepository.findByUsername(username);
        if (user.isEmpty()) {
            return false;
        }

        ResilienceUser existingUser = user.get();
        if (!this.passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
            return false;
        }

        existingUser.setPassword(this.passwordEncoder.encode(newPassword));
        this.resilienceUserRepository.save(existingUser);
        return true;
    }

    public Optional<ResilienceUser> update(String id, AddUserRequest user) {
        Optional<ResilienceUser> existingUser = this.resilienceUserRepository.findById(Long.parseLong(id));
        existingUser.ifPresent(u -> {
            u.setName(user.getName());
            u.setUsername(user.getUsername());
            u.setPassword(this.passwordEncoder.encode(user.getPassword()));
            u.setRole(user.getRole());
            this.resilienceUserRepository.save(u);
        });
        return existingUser;
    }
}
