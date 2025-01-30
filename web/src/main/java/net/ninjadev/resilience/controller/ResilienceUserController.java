package net.ninjadev.resilience.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.ninjadev.resilience.dto.ResilienceUserDTO;
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

    @PostMapping()
    public ResponseEntity<String> addUser(@Valid @RequestBody ResilienceUserDTO user) {
        Optional<ResilienceUser> added = this.resilienceUserService.add(user);
        if (added.isEmpty()) {
            return ResponseEntity.badRequest().body("User exists.");
        }
        return ResponseEntity.ok("User added successfully: " + user.getUsername());
    }

    @DeleteMapping("{username}")
    public ResponseEntity<String> deleteUserByUsername(@PathVariable("username") String username) {
        if (this.resilienceUserService.deleteByUsername(username)) {
            return ResponseEntity.ok("User deleted successfully.");
        }
        return ResponseEntity.ok("No user found to delete.");
    }

    @PutMapping("{username}/password")
    public ResponseEntity<String> updatePassword(@PathVariable("username") String username, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
        if (this.resilienceUserService.updatePassword(username, oldPassword, newPassword)) {
            return ResponseEntity.ok("Password updated successfully.");
        }
        return ResponseEntity.badRequest().body("Invalid old password or user not found.");
    }
}
