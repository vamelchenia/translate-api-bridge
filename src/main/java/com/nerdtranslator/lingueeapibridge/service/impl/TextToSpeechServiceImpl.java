package com.nerdtranslator.lingueeapibridge.service.impl;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import com.nerdtranslator.lingueeapibridge.service.TextToSpeechService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TextToSpeechServiceImpl implements TextToSpeechService {

    private byte[] getSpeechFromText(String textToTransfer, String langCode) {
        byte[] speechResult = null;
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
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
            e.printStackTrace();
        }
        if (speechResult == null) {
            throw new RuntimeException("The result of text to voice translation wasn't received");
        }
        return speechResult;
    }

    @Override
    public byte[] transformTextToSound(String textToTransfer, String langCode) {
        return getSpeechFromText(textToTransfer, langCode);
    }
}
