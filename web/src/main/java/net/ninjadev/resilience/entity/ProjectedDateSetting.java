package net.ninjadev.resilience.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @Column(nullable = false)
    private PreferenceType preferenceType; // "relative", "endOfMonth", or "exact"

    private Integer amount; // For relative preferences (e.g., 7, 30 days)
    private DateUnit unit; // For relative preferences (e.g., "days", "weeks")

    private LocalDate exactDate; // For specific dates (e.g., "2023-11-01")

    public ProjectedDateSetting(
            @NotNull PreferenceType preferenceType,
            Integer amount,
            DateUnit unit,
            LocalDate exactDate
    ) {
        this.preferenceType = preferenceType;
        this.amount = amount;
        this.unit = unit;
        this.exactDate = exactDate;
    }

    public static ProjectedDateSetting createDefault() {
        ProjectedDateSetting setting = new ProjectedDateSetting();
        setting.setPreferenceType(PreferenceType.END_OF_MONTH);
        return setting;
    }

    public LocalDate getProjectedDate() {
        if (preferenceType == null) {
            throw new IllegalStateException("Preference type must be set.");
        }

        return switch (preferenceType) {
            case RELATIVE -> {
                if (unit == null || amount == null) {
                    throw new IllegalStateException("Amount and unit must be set for RELATIVE preferences.");
                }
                yield switch (unit) {
                    case DAYS -> LocalDate.now().plusDays(amount);
                    case WEEKS -> LocalDate.now().plusWeeks(amount);
                    case MONTHS -> LocalDate.now().plusMonths(amount);
                };
            }
            case END_OF_MONTH -> LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
            case EXACT -> {
                if (exactDate == null) {
                    throw new IllegalStateException("Exact date must be set for EXACT preferences.");
                }
                yield exactDate;
            }
        };
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
