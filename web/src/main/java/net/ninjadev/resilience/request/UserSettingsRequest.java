package net.ninjadev.resilience.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.ninjadev.resilience.entity.UserSettings;

@Data
@NoArgsConstructor
public class UserSettingsRequest {

    private ProjectedDateSettingRequest projectedDateSetting;
    private UserSettings.ThemePreference themePreference;
    private String accentColor;

}

