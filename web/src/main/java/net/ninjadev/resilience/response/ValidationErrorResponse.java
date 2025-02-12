package net.ninjadev.resilience.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ValidationErrorResponse {
    private String message;
    private List<String> details;
}
