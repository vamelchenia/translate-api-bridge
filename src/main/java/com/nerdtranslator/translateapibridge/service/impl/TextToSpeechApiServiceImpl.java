package com.nerdtranslator.translateapibridge.service.impl;

import com.google.api.gax.rpc.InvalidArgumentException;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import com.nerdtranslator.translateapibridge.exception.VoiceGenderNotSupportedException;
import com.nerdtranslator.translateapibridge.service.CredentialsProviderFactory;
import com.nerdtranslator.translateapibridge.service.TextToSpeechApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TextToSpeechApiServiceImpl implements TextToSpeechApiService {

    private final CredentialsProviderFactory providerFactory;

    @Override
    public byte[] transformTextToSound(String textToTransfer, String langCode) {
        byte[] speechResult = null;
        AudioEncoding audioEncoding = AudioEncoding.MP3;
        List<SsmlVoiceGender> genders = new ArrayList<>(List.of(SsmlVoiceGender.NEUTRAL, SsmlVoiceGender.FEMALE, SsmlVoiceGender.MALE));
        for (SsmlVoiceGender gender : genders) {
            try (TextToSpeechClient textToSpeechClient = configureTextToSpeechClient()) {
                speechResult = getSpeechResult(textToSpeechClient, gender, audioEncoding, textToTransfer, langCode);
                break;
            } catch (InvalidArgumentException e) {
                continue;
            } catch (IOException e) {
                throw new RuntimeException("\"The result of text to speech translation wasn't received\"");
            }
        }
        if (speechResult == null) {
            throw new VoiceGenderNotSupportedException("\"Voice isn't supported yet\"");
        }

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
