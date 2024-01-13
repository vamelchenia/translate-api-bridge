package com.nerdtranslator.translateapibridge.service.impl;

import com.google.cloud.texttospeech.v1.*;
import com.nerdtranslator.translateapibridge.service.TextToSpeechSynthesizer;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Setter
public class TextToSpeechSynthesizerImpl implements TextToSpeechSynthesizer {

    private TextToSpeechClient textToSpeechClient;
    private SsmlVoiceGender gender;

    @Override
    public SynthesizeSpeechResponse synthesize(String text, AudioEncoding encoding, String langCode) {
        SynthesisInput input = getSynthesisInput(text);
        VoiceSelectionParams voice = getVoiceSelectionParams(gender, langCode);
        AudioConfig audioConfig = getAudioConfig(AudioEncoding.MP3);
        return textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
    }

    private SynthesisInput getSynthesisInput(String textToTransfer) {
        return SynthesisInput.newBuilder()
                .setText(textToTransfer)
                .build();
    }

    private VoiceSelectionParams getVoiceSelectionParams(SsmlVoiceGender gender, String langCode) {
        return VoiceSelectionParams.newBuilder()
                .setLanguageCode(langCode)
                .setSsmlGender(gender)
                .build();
    }

    private AudioConfig getAudioConfig(AudioEncoding encoding) {
        return AudioConfig.newBuilder()
                .setAudioEncoding(encoding)
                .build();
    }
}
