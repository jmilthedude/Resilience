package net.ninjadev.resilience.controller;

import jakarta.validation.Valid;
import net.ninjadev.resilience.entity.ResilienceUser;
import net.ninjadev.resilience.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@Valid @RequestBody ResilienceUser user) {
        Optional<ResilienceUser> added = this.userService.add(user);
        if (added.isEmpty()) {
            return ResponseEntity.badRequest().body("User exists.");
        }
        return ResponseEntity.ok("User added successfully: " + user.getUsername());
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteUserByUsername(@PathVariable String username) {
        if (this.userService.deleteByUsername(username)) {
            return ResponseEntity.ok("User deleted successfully.");
        }
        return ResponseEntity.ok("No user found to delete.");
    }
}
