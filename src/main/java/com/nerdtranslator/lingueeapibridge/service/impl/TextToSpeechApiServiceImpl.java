package com.nerdtranslator.lingueeapibridge.service.impl;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import com.nerdtranslator.lingueeapibridge.service.CredentialsProviderFactory;
import com.nerdtranslator.lingueeapibridge.service.TextToSpeechApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TextToSpeechApiServiceImpl implements TextToSpeechApiService {

    private final CredentialsProviderFactory authenticationProvider;

    @Override
    public byte[] transformTextToSound(String textToTransfer, String langCode) {
        byte[] speechResult;

        try (TextToSpeechClient textToSpeechClient =
                     TextToSpeechClient
                             .create(TextToSpeechSettings
                                     .newBuilder()
                                     .setCredentialsProvider(authenticationProvider.getCredentialsProvider())
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
