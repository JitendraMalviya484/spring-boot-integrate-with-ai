package code.with.jeetu.ai.dto;

public record ChatResponse(
        String id,
        String content,
        String model,
        double promptTokens,
        double completionTokens,
        double totalTokens
) {}

