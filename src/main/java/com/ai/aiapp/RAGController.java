package com.ai.aiapp;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RAGController {
    private ChatClient client;

    @Value("classpath:/prompts/prompt.st")
    private Resource promptResource;

    private VectorStore vectorStore;

    public RAGController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.client = builder.build();
        this.vectorStore = vectorStore;
    }

    @GetMapping(value = "/chat", produces = MediaType.TEXT_PLAIN_VALUE)
    public String chat(@RequestParam(name = "question") String question) {
        PromptTemplate promptTemplate = new PromptTemplate(promptResource);
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.query(question).withTopK(1)
        );
        List<String> context = documents.stream().map(Document::getContent).toList();
        Prompt prompt = promptTemplate.create(Map.of("context", context, "question", question));
        return client.prompt(prompt).call().content();
    }

}
