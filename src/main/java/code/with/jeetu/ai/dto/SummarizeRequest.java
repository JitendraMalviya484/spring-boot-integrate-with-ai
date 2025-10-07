package code.with.jeetu.ai.dto;

import jakarta.validation.constraints.NotBlank;

public record SummarizeRequest(
        @NotBlank String text
) {}
