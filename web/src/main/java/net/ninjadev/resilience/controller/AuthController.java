package net.ninjadev.resilience.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @RequestMapping("/status")
    public ResponseEntity<AuthStatusResponse> status(Authentication authentication) {
        return ResponseEntity.ok(new AuthStatusResponse(authentication != null && authentication.isAuthenticated()));
    }

    public record AuthStatusResponse(boolean authenticated) {}
}
