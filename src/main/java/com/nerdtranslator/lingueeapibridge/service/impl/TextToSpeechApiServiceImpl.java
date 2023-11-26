package com.nerdtranslator.lingueeapibridge.service.impl;

import com.google.api.gax.core.CredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import com.nerdtranslator.lingueeapibridge.service.AuthenticationDataProvider;
import com.nerdtranslator.lingueeapibridge.service.TextToSpeechApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class TextToSpeechApiServiceImpl implements TextToSpeechApiService {

    private final AuthenticationDataProvider authenticationProvider;

    @Autowired
    public TextToSpeechApiServiceImpl(AuthenticationDataProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public byte[] transformTextToSound(String textToTransfer, String langCode) {
        byte[] speechResult;
        CredentialsProvider credentialsProvider = () -> {
            try (ByteArrayInputStream keyStream = new ByteArrayInputStream(authenticationProvider.getAuthenticationData())) {
                return ServiceAccountCredentials.fromStream(keyStream);
            }
        };

        try (TextToSpeechClient textToSpeechClient =
                     TextToSpeechClient
                             .create(TextToSpeechSettings
                                     .newBuilder()
                                     .setCredentialsProvider(credentialsProvider)
                                     .build())) {
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(textToTransfer)
                    .build();

            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode(langCode)
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .build();

            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
            ByteString audioRepresentationOfText = response.getAudioContent();
            speechResult = audioRepresentationOfText.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("An error with text to speech service");
        }
        if (speechResult == null) {
            throw new RuntimeException("The result of text to speech translation wasn't received");
        }
        return speechResult;
    }
}
