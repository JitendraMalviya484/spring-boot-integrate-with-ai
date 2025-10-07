package code.with.jeetu.ai.service;



import code.with.jeetu.ai.dto.ChatResponse;
import code.with.jeetu.ai.openai.ChatCompletions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OpenAiService {

    private final WebClient openAiWebClient;
    private final String defaultModel;
    private final Double defaultTemperature;

    public OpenAiService(WebClient openAiWebClient,
                         @Value("${openai.model:gpt-4o-mini}") String defaultModel,
                         @Value("${openai.temperature:0.7}") Double defaultTemperature) {
        this.openAiWebClient = openAiWebClient;
        this.defaultModel = defaultModel;
        this.defaultTemperature = defaultTemperature;
    }

    public Mono<ChatResponse> chat(String systemPrompt, String userMessage) {
        ChatCompletions.Request request = new ChatCompletions.Request(
                defaultModel,
                List.of(
                        new ChatCompletions.Message("system", systemPrompt),
                        new ChatCompletions.Message("user", userMessage)
                ),
                defaultTemperature,
                null,
                null
        );

        return openAiWebClient.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatCompletions.Response.class)
                .map(resp -> {
                    String content = resp.choices != null && !resp.choices.isEmpty()
                            ? resp.choices.get(0).message.content()
                            : "";
                    ChatCompletions.Response.Usage u = resp.usage;
                    double pt = u != null ? u.prompt_tokens : 0;
                    double ct = u != null ? u.completion_tokens : 0;
                    double tt = u != null ? u.total_tokens : 0;
                    return new ChatResponse(resp.id, content, resp.model, pt, ct, tt);
                });
    }

    public Mono<ChatResponse> summarize(String text) {
        String systemPrompt = "You are a concise assistant. Summarize the user's text into 3-5 bullet points.";
        return chat(systemPrompt, text);
    }

    public Mono<ChatResponse> recommend(String profile, String itemsCsv) {
        String sys = "You are a recommendation engine. Suggest 3-5 items with reasons.";
        String user = "Profile:\n" + profile + "\n\nItems:\n" + itemsCsv;
        return chat(sys, user);
    }
}

