package net.ninjadev.resilience.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ninjadev.resilience.entity.ResilienceUser;
import net.ninjadev.resilience.request.AddUserRequest;
import net.ninjadev.resilience.request.UpdatePasswordRequest;
import net.ninjadev.resilience.response.ResilienceUserResponse;
import net.ninjadev.resilience.security.PasswordAuthorization;
import net.ninjadev.resilience.service.ResilienceUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class ResilienceUserController {

    private final ResilienceUserService resilienceUserService;
    private final PasswordAuthorization passwordAuthorization;

    @GetMapping("{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResilienceUserResponse> getUser(@PathVariable("username") String username) {
        return this.resilienceUserService.findByUsername(username)
                .map(user -> ResponseEntity.ok(new ResilienceUserResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ResilienceUserResponse>> listUsers() {
        return ResponseEntity.ok(this.resilienceUserService.getAllUsers().stream().map(ResilienceUserResponse::new).toList());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addUser(@Valid @RequestBody AddUserRequest user) {
        Optional<ResilienceUser> added = this.resilienceUserService.add(user);
        if (added.isEmpty()) {
            return ResponseEntity.badRequest().body("User exists.");
        }
        return ResponseEntity.ok("User added successfully: " + user.getUsername());
    }

    @DeleteMapping("{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUserByUsername(@PathVariable("username") String username) {
        if (this.resilienceUserService.deleteByUsername(username)) {
            return ResponseEntity.ok("User deleted successfully.");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("{username}/password")
    public ResponseEntity<String> updatePassword(@PathVariable("username") String username, @RequestBody @Valid UpdatePasswordRequest request, Authentication authentication) {
        String authenticatedUser = authentication.getName();
        if (!this.passwordAuthorization.canUpdatePassword(username, authentication)) {
            log.warn("Failed password update attempt. Authenticated User={}, Targeted User={}", authenticatedUser, username);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User '%s' is not permitted to change this password.".formatted(authenticatedUser));
        }
        if (this.resilienceUserService.updatePassword(username, request.getCurrentPassword(), request.getNewPassword())) {
            log.info("Password updated. Authenticated User={}, Targeted User={}", authenticatedUser, username);
            return ResponseEntity.ok("Password updated successfully.");
        }
        return ResponseEntity.badRequest().body("Invalid old password or user not found.");
    }
}
