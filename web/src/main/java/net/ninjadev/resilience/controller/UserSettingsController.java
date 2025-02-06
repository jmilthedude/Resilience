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
                .orElseGet(() -> {
                    log.warn("User settings not found for username: {}", authentication.getName());
                    return ResponseEntity.notFound().build();
                });
    }

    @PatchMapping
    public ResponseEntity<UserSettings> updateUserSettings(Authentication authentication, @RequestBody UserSettingsRequest request) {
        userSettingsService.patchSettings(request, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
