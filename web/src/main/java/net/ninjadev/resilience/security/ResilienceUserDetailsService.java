package net.ninjadev.resilience.security;

import lombok.extern.slf4j.Slf4j;
import net.ninjadev.resilience.entity.ResilienceUser;
import net.ninjadev.resilience.repository.ResilienceUserRepository;
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
import java.util.Optional;

@Slf4j
@Service
public class ResilienceUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final String adminPassword;
    private final ResilienceUserRepository resilienceUserRepository;

    public ResilienceUserDetailsService(PasswordEncoder passwordEncoder, ResilienceUserRepository resilienceUserRepository) throws IOException {
        this.passwordEncoder = passwordEncoder;
        this.resilienceUserRepository = resilienceUserRepository;

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
        if ("admin".equals(username)) {
            return User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode(this.adminPassword))
                    .roles(ResilienceUser.Role.ADMIN.toString())
                    .build();
        }
        Optional<ResilienceUser> userOptional = this.resilienceUserRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        ResilienceUser user = userOptional.get();
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().toString())
                .build();
    }
}
