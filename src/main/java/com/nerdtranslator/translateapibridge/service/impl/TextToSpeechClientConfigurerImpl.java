package com.nerdtranslator.translateapibridge.service.impl;

import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.TextToSpeechSettings;
import com.nerdtranslator.translateapibridge.service.CredentialsProviderFactory;
import com.nerdtranslator.translateapibridge.service.TextToSpeechClientConfigurer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TextToSpeechClientConfigurerImpl implements TextToSpeechClientConfigurer {

    private final CredentialsProviderFactory providerFactory;

    @Override
    public TextToSpeechClient configure() {
        TextToSpeechClient textToSpeechClient = null;

        try {
            textToSpeechClient = TextToSpeechClient
                    .create(TextToSpeechSettings
                            .newBuilder()
                            .setCredentialsProvider(providerFactory.getCredentialsProvider())
                            .build());
        } catch (IOException e) {
            throw new RuntimeException("An error with text to speech service");
        }

        return textToSpeechClient;
    }
}
