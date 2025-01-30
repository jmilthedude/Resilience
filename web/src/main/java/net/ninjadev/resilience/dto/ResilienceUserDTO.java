package net.ninjadev.resilience.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ninjadev.resilience.entity.ResilienceUser;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResilienceUserDTO {
    @NotEmpty
    private String username;
    @Size(min = 8, max = 20)
    private String password;
    @NotNull
    private ResilienceUser.Role role;

    public ResilienceUserDTO(@Valid ResilienceUser user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
    }
}
