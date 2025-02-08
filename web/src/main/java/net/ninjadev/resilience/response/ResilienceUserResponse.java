package net.ninjadev.resilience.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ninjadev.resilience.entity.ResilienceUser;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResilienceUserResponse {
    private long id;
    private String username;
    private String name;
    private ResilienceUser.Role role;

    public ResilienceUserResponse(ResilienceUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.role = user.getRole();
    }
}
