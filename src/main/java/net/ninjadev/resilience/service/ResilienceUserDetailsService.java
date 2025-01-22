package net.ninjadev.resilience.service;

import lombok.extern.slf4j.Slf4j;
import net.ninjadev.resilience.entity.ResilienceUser;
import net.ninjadev.resilience.repository.UserRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class ResilienceUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final String adminPassword;
    private final UserRepository userRepository;

    public ResilienceUserDetailsService(PasswordEncoder passwordEncoder, UserRepository userRepository) throws IOException {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;

        if (isProduction()) {
            this.adminPassword = System.getenv("ADMIN_PASSWORD");
            if (this.adminPassword == null) {
                throw new IllegalStateException("ADMIN_PASSWORD environment variable is not set in production");
            }
        } else {
            ClassPathResource resource = new ClassPathResource("admin-password.txt");
            try (InputStream inputStream = resource.getInputStream()) {
                this.adminPassword = new String(FileCopyUtils.copyToByteArray(inputStream)).trim();
            }
        }
    }

    private boolean isProduction() {
        String env = System.getProperty("spring.profiles.active");
        return "prod".equalsIgnoreCase(env);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Authenticating: {}", username);
        log.info("Admin Role: {}", ResilienceUser.Role.ADMIN);
        if ("admin".equals(username)) {
            return User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode(this.adminPassword))
                    .roles(ResilienceUser.Role.ADMIN.toString())
                    .build();
        }
        ResilienceUser user = this.userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().toString())
                .build();
    }
}
