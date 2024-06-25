package com.ai.aiapp;

/*import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import java.util.List;*/

public class TestOpenAi {
    public static void main(String[] args) {
        /*
        OpenAiApi openAiApi = new OpenAiApi("sk-proj-cY8qbi9oW4gu13vNUKuYT3BlbkFJAGH0gautgdh3RfgYUw5c");
        OpenAiChatModel openAiChatModel = new OpenAiChatModel(openAiApi, OpenAiChatOptions.builder()
                .withModel("gpt-3.5-turbo")
                .withMaxTokens(4000)
                .withTemperature(0F)
                .build()
        );
        SystemMessage systemMessage = new SystemMessage("""
                Donne moi l'équipe qui a gagné la coupe du monde de football dans l'année qui vous sera fournit.
                Je veux le résultat au format JSON et qui contient : 
                  - Nom de l'équipe gagnante
                  - Nom des joueurs
                  - le pays organisateur
                 """);
        UserMessage userMessage = new UserMessage("Je veux le résultat pour l'année 2022");
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        ChatResponse chatResponse = openAiChatModel.call(prompt);
        System.out.println(chatResponse.getResult().getOutput().getContent());
         */
    }
}
