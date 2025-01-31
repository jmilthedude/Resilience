package net.ninjadev.resilience.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import net.ninjadev.resilience.request.AddUserRequest;
import net.ninjadev.resilience.entity.ResilienceUser;
import net.ninjadev.resilience.repository.ResilienceUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResilienceUserService {

    private final ResilienceUserRepository resilienceUserRepository;
    private final PasswordEncoder passwordEncoder;

    public ResilienceUserService(ResilienceUserRepository resilienceUserRepository, PasswordEncoder passwordEncoder) {
        this.resilienceUserRepository = resilienceUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
}
