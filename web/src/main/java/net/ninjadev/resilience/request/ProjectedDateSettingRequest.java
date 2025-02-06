package net.ninjadev.resilience.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.ninjadev.resilience.entity.ProjectedDateSetting;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ProjectedDateSettingRequest {

    private ProjectedDateSetting.PreferenceType preferenceType;
    private Integer amount;
    private ProjectedDateSetting.DateUnit unit;
    private LocalDate exactDate;
}

