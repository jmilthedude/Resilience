package net.ninjadev.resilience.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] OPEN_URLS = {
            "/api/v1/login",
            "/user-login",
            "/api/v1/auth/status",
            "/register",
            "/static/**",
            "/error",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(getHttpRequestsCustomizer())
                .formLogin(getFormLoginCustomizer())
                .logout(logout -> logout
                        .logoutUrl("/api/v1/logout")
                        .logoutSuccessHandler(((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                        }))
                        .permitAll())
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    private Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> getHttpRequestsCustomizer() {
        return authorize -> authorize
                .requestMatchers(OPEN_URLS).permitAll()
                .anyRequest().authenticated();
    }

    private Customizer<FormLoginConfigurer<HttpSecurity>> getFormLoginCustomizer() {
        return form -> form
                .loginProcessingUrl("/api/v1/login")
                .successHandler(((request, response, authentication) -> {
                    response.setStatus(HttpStatus.OK.value());
                }))
                .permitAll();
    }
}
