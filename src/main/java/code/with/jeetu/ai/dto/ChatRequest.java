package code.with.jeetu.ai.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(
        @NotBlank String user,
        @NotBlank String message
) {}
