package net.ninjadev.resilience.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ninjadev.resilience.config.ResilienceConfiguration;
import net.ninjadev.resilience.entity.ProjectedDateSetting;
import net.ninjadev.resilience.entity.ResilienceUser;
import net.ninjadev.resilience.entity.UserSettings;
import net.ninjadev.resilience.repository.ResilienceUserRepository;
import net.ninjadev.resilience.request.ProjectedDateSettingRequest;
import net.ninjadev.resilience.request.UserSettingsRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSettingsService {

    private final ResilienceUserRepository resilienceUserRepository;
    private final ResilienceConfiguration configuration;

    public Optional<UserSettings> getUserSettings(String username) {
        return this.resilienceUserRepository.findByUsername(username)
                .map(ResilienceUser::getUserSettings)
                .or(() -> Optional.of(UserSettings.createDefault(configuration)));
    }

    @Transactional
    public void patchSettings(UserSettingsRequest request, String username) {
        ResilienceUser user = resilienceUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserSettings settings = user.getUserSettings();
        if (settings == null) {
            settings = UserSettings.createDefault(configuration);
            user.setUserSettings(settings);
        }

        if (request.getThemePreference() != null) {
            settings.setThemePreference(request.getThemePreference());
        }

        if (request.getAccentColor() != null) {
            settings.setAccentColor(request.getAccentColor());
        }

        if (request.getProjectedDateSetting() != null) {
            ProjectedDateSetting existingSetting = settings.getProjectedDateSetting();
            if (existingSetting == null) {
                existingSetting = ProjectedDateSetting.createDefault();
                settings.setProjectedDateSetting(existingSetting);
            }
            updateProjectedDateSetting(request.getProjectedDateSetting(), existingSetting);
        }

        resilienceUserRepository.save(user);
    }

    private void updateProjectedDateSetting(ProjectedDateSettingRequest request, ProjectedDateSetting existingSetting) {
        validateProjectedDateSetting(request);
        if (request.getPreferenceType() != null) {
            existingSetting.setPreferenceType(request.getPreferenceType());
        }

        if (request.getAmount() != null) {
            existingSetting.setAmount(request.getAmount());
        }

        if (request.getUnit() != null) {
            existingSetting.setUnit(request.getUnit());
        }

        if (request.getExactDate() != null) {
            existingSetting.setExactDate(request.getExactDate());
        }
    }

    private void validateProjectedDateSetting(ProjectedDateSettingRequest request) {
        if (request.getPreferenceType() == ProjectedDateSetting.PreferenceType.RELATIVE) {
            if (request.getAmount() == null || request.getUnit() == null) {
                throw new IllegalArgumentException("Amount and unit must be set for RELATIVE preferences.");
            }
        } else if (request.getPreferenceType() == ProjectedDateSetting.PreferenceType.EXACT) {
            if (request.getExactDate() == null || request.getExactDate().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Exact date must be set and in the future for EXACT preferences.");
            }
        }
    }


}
