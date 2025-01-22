package net.ninjadev.resilience.controller;

import lombok.extern.slf4j.Slf4j;
import net.ninjadev.resilience.config.ResilienceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

    private final ResilienceConfiguration configuration;

    @Autowired
    public HomeController(ResilienceConfiguration configuration) {
        this.configuration = configuration;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        String message = "Hello, from %s".formatted(this.configuration.getApplicationName());
        log.info(message);
        return ResponseEntity.ok(message);
    }
}
