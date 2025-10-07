package code.with.jeetu.ai.controller;



import code.with.jeetu.ai.dto.ChatRequest;
import code.with.jeetu.ai.dto.ChatResponse;
import code.with.jeetu.ai.dto.SummarizeRequest;
import code.with.jeetu.ai.service.OpenAiService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final OpenAiService openAiService;

    public AiController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping(path = "/chat", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        String systemPrompt = "You are ChatGPT integrated inside a Java backend.";
        String userMessage = request.user() + " says: " + request.message();
        return openAiService.chat(systemPrompt, userMessage);
    }

    @PostMapping(path = "/summarize", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ChatResponse> summarize(@Valid @RequestBody SummarizeRequest request) {
        return openAiService.summarize(request.text());
    }

    @GetMapping(path = "/recommendations", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ChatResponse> recommend(
            @RequestParam(defaultValue = "senior Java developer") String profile,
            @RequestParam(defaultValue = "Spring in Action,Effective Java") String items) {
        return openAiService.recommend(profile, items);
    }
}

