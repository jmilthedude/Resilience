package net.ninjadev.resilience.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ninjadev.resilience.config.ResilienceConfiguration;
import net.ninjadev.resilience.request.AddUserRequest;

import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class ResilienceUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "UserName cannot be empty")
    private String username;

    @JsonIgnore
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @JsonIgnore
    @NotNull(message = "Role cannot be empty")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_settings_id", referencedColumnName = "id")
    private UserSettings userSettings;

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    private Set<Account> accounts;

    public ResilienceUser(@Valid AddUserRequest user, ResilienceConfiguration configuration) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.userSettings = UserSettings.createDefault(configuration);
    }

    public enum Role {
        ADMIN,
        USER
    }

}
