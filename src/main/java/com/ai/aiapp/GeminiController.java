package com.ai.aiapp;

import com.ai.aiapp.model.Team;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class GeminiController {

    private final ChatClient client;

    public GeminiController(ChatClient.Builder client) {
        this.client = client.build();
    }

    @GetMapping(value = "/ai/generate2", produces = MediaType.TEXT_PLAIN_VALUE)
    public String generate2(@RequestParam(value = "message", defaultValue = "Go") String message) {
        return client.prompt()
                .system("""
                        Donnez le code sous format shell, sans explication du code ni rien du tout 
                        uniquement le code
                        """)
                .user("Je veux un code qui permet de créer une liste de 5 nombres aléatoires," +
                        " il les affiches, puis il fait le tri croissant et le réafiche. Avec le langage "+message)
                .call()
                .content();
    }

    @GetMapping(value = "/ai/generate")
    public Team generate(@RequestParam(value = "message", defaultValue = "2022") String message) {
        String systemMessage = """
            Donne moi l'équipe qui a gagné la coupe du monde de football dans l'année qui vous sera fournit.
            """;

        String userMessage = "Je veux le résultat pour l'année "+message;
        return client.prompt()
                .system(systemMessage)
                .user(userMessage)
                .call()
                .entity(Team.class);
    }

    @GetMapping(value = "/ai/generate3")
    public List<Team> generate3() {
        String systemMessage = """
            Donne moi les équipes qui ont gagné la coupe du monde de football plus que 3 fois.
            """;

        return client.prompt()
                .system(systemMessage)
                .call()
                .entity(new ParameterizedTypeReference<List<Team>>() {});
    }

    @GetMapping(value = "/ocr", produces = MediaType.TEXT_PLAIN_VALUE)
    public String ocr() throws IOException {
        Resource resource = new ClassPathResource("img.png");
        byte[] bytes = resource.getContentAsByteArray();
        String userMessageText = """
               Analyse l'image qui donne du texte manuscrit et donne moi,
               au format json comment les dépenses de la semaine sont réparties
               et combien d'argent reste.
               donne moi aussi le salaire que je reçois.
        """;

        UserMessage userMessage = new UserMessage(userMessageText, List.of(
                new Media(MimeTypeUtils.IMAGE_PNG, bytes)
        ));

        Prompt prompt = new Prompt(userMessage);

        return client.prompt(prompt)
                .call()
                .content();
    }

}