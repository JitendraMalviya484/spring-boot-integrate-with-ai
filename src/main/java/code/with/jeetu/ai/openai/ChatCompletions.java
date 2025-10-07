package code.with.jeetu.ai.openai;

import java.util.List;

public class ChatCompletions {

    public record Message(String role, String content) {}

    public record Request(
            String model,
            List<Message> messages,
            Double temperature,
            Double top_p,
            Integer max_tokens
    ) {}

    public static class Response {
        public String id;
        public String object;
        public long created;
        public String model;
        public List<Choice> choices;
        public Usage usage;

        public static class Choice {
            public int index;
            public Message message;
            public String finish_reason;
        }

        public static class Usage {
            public int prompt_tokens;
            public int completion_tokens;
            public int total_tokens;
        }
    }
}

