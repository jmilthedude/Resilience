package net.ninjadev.resilience.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProjectedDateSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PreferenceType preferenceType; // "relative", "endOfMonth", or "exact"
    private Integer amount; // For relative preferences (e.g., 7, 30 days)
    private DateUnit unit; // For relative preferences (e.g., "days", "weeks")
    private LocalDate exactDate; // For specific dates (e.g., "2023-11-01")

    public static ProjectedDateSetting createDefault() {
        ProjectedDateSetting setting = new ProjectedDateSetting();
        setting.setPreferenceType(PreferenceType.END_OF_MONTH);
        return setting;
    }

    public LocalDate getProjectedDate() {
        if (preferenceType == PreferenceType.RELATIVE) {
            switch (unit) {
                case DAYS -> {
                    return LocalDate.now().plusDays(amount);
                }
                case WEEKS -> {
                    return LocalDate.now().plusWeeks(amount);
                }
                case MONTHS -> {
                    return LocalDate.now().plusMonths(amount);
                }
            }
            return LocalDate.now().plusDays(amount);
        } else if (preferenceType == PreferenceType.END_OF_MONTH) {
            return LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        } else {
            return exactDate;
        }
    }

    public enum PreferenceType {
        RELATIVE,
        END_OF_MONTH,
        EXACT
    }

    public enum DateUnit {
        DAYS,
        WEEKS,
        MONTHS
    }
}
