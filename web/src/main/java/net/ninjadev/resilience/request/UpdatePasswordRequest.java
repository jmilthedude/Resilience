package net.ninjadev.resilience.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordRequest {
    @Size(min = 8, max = 20)
    private String currentPassword;
    @Size(min = 8, max = 20)
    private String newPassword;
}
