package net.ninjadev.resilience.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ninjadev.resilience.entity.UserSettings;
import net.ninjadev.resilience.request.UserSettingsRequest;
import net.ninjadev.resilience.service.UserSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-settings")
public class UserSettingsController {
    private final UserSettingsService userSettingsService;

    @GetMapping
    public ResponseEntity<UserSettings> getUserSettings(Authentication authentication) {
        return userSettingsService.getUserSettings(authentication.getName())
                .map(ResponseEntity::ok)
                .orElseGet(() -> getNotFoundResponseForUserSettings(authentication.getName()));
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserSettings> getUserSettingsByUsername(@PathVariable String username) {
        return userSettingsService.getUserSettings(username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> getNotFoundResponseForUserSettings(username));
    }

    @PatchMapping
    public ResponseEntity<UserSettings> updateUserSettings(Authentication authentication, @RequestBody UserSettingsRequest request) {
        userSettingsService.patchSettings(request, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<UserSettings> getNotFoundResponseForUserSettings(String authentication) {
        log.warn("User settings not found for username: {}", authentication);
        return ResponseEntity.notFound().build();
    }
}
