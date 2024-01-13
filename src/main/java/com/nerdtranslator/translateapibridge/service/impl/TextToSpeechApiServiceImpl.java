package com.nerdtranslator.translateapibridge.service.impl;

import com.google.api.gax.rpc.InvalidArgumentException;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.protobuf.ByteString;
import com.nerdtranslator.translateapibridge.service.TextToSpeechApiService;
import com.nerdtranslator.translateapibridge.service.TextToSpeechClientConfigurer;
import com.nerdtranslator.translateapibridge.service.TextToSpeechSynthesizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TextToSpeechApiServiceImpl implements TextToSpeechApiService {

    private final TextToSpeechClientConfigurer textToSpeechClientConfigurer;
    private final TextToSpeechSynthesizer speechSynthesizer;

    @Override
    public byte[] transformTextToSound(String textToTransfer, String langCode) {
        byte[] speechResult = null;
        SynthesizeSpeechResponse speechResponse = null;
        TextToSpeechClient textToSpeechClient = null;

        try {
            textToSpeechClient = textToSpeechClientConfigurer.configure();
            speechSynthesizer.setTextToSpeechClient(textToSpeechClient);
            speechSynthesizer.setGender(SsmlVoiceGender.NEUTRAL);

            speechResponse = speechSynthesizer.synthesize(textToTransfer, AudioEncoding.MP3, langCode);
            ByteString audioRepresentationOfText = speechResponse.getAudioContent();
            speechResult = audioRepresentationOfText.toByteArray();
        } catch (InvalidArgumentException e) {
            if (textToSpeechClient != null) {
                speechSynthesizer.setGender(SsmlVoiceGender.FEMALE);
                speechResponse = speechSynthesizer.synthesize(textToTransfer, AudioEncoding.MP3, langCode);
                ByteString audioRepresentationOfText = speechResponse.getAudioContent();
                speechResult = audioRepresentationOfText.toByteArray();
            }
        } finally {
            if (textToSpeechClient != null) {
                textToSpeechClient.close();
            }
        }
        if (speechResult == null) {
            throw new RuntimeException("The result of text to speech translation wasn't received");
        }
        return speechResult;
    }
}
