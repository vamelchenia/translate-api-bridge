package com.nerdtranslator.translateapibridge.service.impl;

import com.google.api.gax.rpc.InvalidArgumentException;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import com.nerdtranslator.translateapibridge.exception.GoogleApiResponseException;
import com.nerdtranslator.translateapibridge.service.CredentialsProviderFactory;
import com.nerdtranslator.translateapibridge.service.TextToSpeechApiService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TextToSpeechApiServiceImpl implements TextToSpeechApiService {

    private final CredentialsProviderFactory providerFactory;
    private static final Logger log = LoggerFactory.getLogger(TextToSpeechApiServiceImpl.class);

    @Override
    public byte[] transformTextToSound(String textToTransfer, String langCode) {
        log.info("TextToSpeechApiServiceImpl transformTextToSound start");
        byte[] speechResult = null;
        AudioEncoding audioEncoding = AudioEncoding.MP3;
        List<SsmlVoiceGender> genders = new ArrayList<>(
                List.of(SsmlVoiceGender.NEUTRAL, SsmlVoiceGender.FEMALE, SsmlVoiceGender.MALE));
        for (SsmlVoiceGender gender : genders) {
            try (TextToSpeechClient textToSpeechClient = configureTextToSpeechClient()) {
                speechResult = getSpeechResult(textToSpeechClient, gender, audioEncoding, textToTransfer, langCode);
                break;
            } catch (InvalidArgumentException e) {
                log.warn("Voice gender " + gender + " isn't supported, trying the next one", e);
            } catch (IOException e) {
                log.error("An error occurred in text to speech API response", e);
                throw new GoogleApiResponseException("text to speech API response: " + e.getMessage());
            }
        }
        if (speechResult == null) {
            log.error("An error occurred in text to speech API response: voice isn't supported yet");
            throw new GoogleApiResponseException("text to speech API response: voice isn't supported yet");
        }
        log.info("TextToSpeechApiServiceImpl transformTextToSound end");
        return speechResult;
    }

    private byte[] getSpeechResult(TextToSpeechClient textToSpeechClient,
                                   SsmlVoiceGender gender, AudioEncoding audioEncoding,
                                   String textToTransfer, String langCode) {
        SynthesisInput input = getSynthesisInput(textToTransfer);
        VoiceSelectionParams voice = getVoiceSelectionParams(gender, langCode);
        AudioConfig audioConfig = getAudioConfig(audioEncoding);
        SynthesizeSpeechResponse speechResponse = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
        ByteString audioRepresentationOfText = speechResponse.getAudioContent();
        return audioRepresentationOfText.toByteArray();
    }

    private TextToSpeechClient configureTextToSpeechClient() throws IOException {
        return TextToSpeechClient.create(TextToSpeechSettings
                .newBuilder()
                .setCredentialsProvider(providerFactory.getCredentialsProvider())
                .build());
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