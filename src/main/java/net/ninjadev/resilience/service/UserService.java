package net.ninjadev.resilience.service;

import jakarta.transaction.Transactional;
import net.ninjadev.resilience.entity.ResilienceUser;
import net.ninjadev.resilience.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<ResilienceUser> add(ResilienceUser user) {
        if (this.userRepository.findByUsername(user.getUsername()).isPresent()) {
            return Optional.empty();
        }

        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);  // Set the encoded password

        this.userRepository.save(user);
        return Optional.of(user);
    }

    @Transactional
    public boolean deleteByUsername(String username) {
        if (this.userRepository.findByUsername(username).isEmpty()) {
            return false;
        }
        this.userRepository.deleteByUsername(username);
        return true;
    }
}
