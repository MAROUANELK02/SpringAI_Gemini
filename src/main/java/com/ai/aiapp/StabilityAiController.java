package com.ai.aiapp;

import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.stabilityai.StabilityAiImageModel;
import org.springframework.ai.stabilityai.api.StabilityAiImageOptions;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
public class StabilityAiController {
    private final StabilityAiImageModel client;

    public StabilityAiController(StabilityAiImageModel client) {
        this.client = client;
    }

    @GetMapping(value = "/generateImage", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] generateImage() {
        ImageResponse response = client.call(
                new ImagePrompt("A cat in party with a costume and a cupcoffe in his right hand",
                        StabilityAiImageOptions.builder()
                                .withStylePreset("cinematic")
                                .withN(4)
                                .withHeight(1024)
                                .withWidth(1024).build())
        );

        String image = response.getResult().getOutput().getB64Json();
        return Base64.getDecoder().decode(image);
    }

}
