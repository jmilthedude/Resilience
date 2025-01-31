package net.ninjadev.resilience.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PasswordAuthorization {

    public boolean canUpdatePassword(String username, Authentication authentication) {
        if(authentication.getName().equals(username)) {
            log.info("Requester is changing their own password. Allowing password update.");
            return true;
        }
        if(hasAdminRole(authentication)) {
            log.info("Requester is an admin. Allowing password update.");
            return true;
        }
        return false;
    }

    public boolean hasAdminRole(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_ADMIN"));
    }

}
