package com.ai.aiapp;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.function.Function;

@RestController
public class GeminiController {
    private final VertexAiGeminiChatModel chatModel;

    record Transaction(String id) {
    }

    record Status(String name) {
    }

    private static final Map<Transaction, Status> DATASET =
            Map.of(
                    new Transaction("001"), new Status("pending"),
                    new Transaction("002"), new Status("approved"),
                    new Transaction("003"), new Status("rejected"));

    @Bean
    @Description("Get the status of a payment transaction")
    public Function<Transaction, Status> paymentStatus() {
        return transaction -> DATASET.get(transaction);
    }

    @Autowired
    public GeminiController(VertexAiGeminiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/ai/generate")
    public String generate() {
        return chatModel.call(new Prompt("What is the status of my payment transaction 003?")).toString();
    }

    @GetMapping("/ai/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatModel.stream(prompt);
    }
}
