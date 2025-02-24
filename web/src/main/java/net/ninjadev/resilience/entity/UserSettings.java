package net.ninjadev.resilience.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ninjadev.resilience.config.ResilienceConfiguration;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private ProjectedDateSetting projectedDateSetting;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ThemePreference themePreference;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Invalid hex color value")
    private String accentColor;

    public static UserSettings createDefault(ResilienceConfiguration configuration) {
        UserSettings settings = new UserSettings();
        settings.setProjectedDateSetting(ProjectedDateSetting.createDefault());
        settings.setThemePreference(UserSettings.ThemePreference.DARK);
        settings.setAccentColor(configuration.getDefaultAccentColor());
        return settings;
    }

    public enum ThemePreference {
        LIGHT,
        DARK
    }

}
