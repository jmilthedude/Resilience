package net.ninjadev.resilience.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.ninjadev.resilience.entity.ResilienceUser;
import net.ninjadev.resilience.service.ResilienceUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class ResilienceUserController {

    private final ResilienceUserService resilienceUserService;

    @Autowired
    public ResilienceUserController(ResilienceUserService resilienceUserService) {
        this.resilienceUserService = resilienceUserService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@Valid @RequestBody AddUserRequest user) {
        log.info(user.toString());
        Optional<ResilienceUser> added = this.resilienceUserService.add(new ResilienceUser(user.username, user.password, user.role));
        if (added.isEmpty()) {
            return ResponseEntity.badRequest().body("User exists.");
        }
        return ResponseEntity.ok("User added successfully: " + user.username);
    }

    public record AddUserRequest(String username, String password, ResilienceUser.Role role){}

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteUserByUsername(@PathVariable String username) {
        if (this.resilienceUserService.deleteByUsername(username)) {
            return ResponseEntity.ok("User deleted successfully.");
        }
        return ResponseEntity.ok("No user found to delete.");
    }
}
