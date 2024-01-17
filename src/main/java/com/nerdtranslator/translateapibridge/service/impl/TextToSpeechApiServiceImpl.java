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
        TextToSpeechClient textToSpeechClient = null;
        AudioEncoding audioEncoding = null;
        try {
            audioEncoding = AudioEncoding.MP3;
            textToSpeechClient = configureTextToSpeechClient();
            speechResult = getSpeechResult(textToSpeechClient, SsmlVoiceGender.NEUTRAL,
                    audioEncoding, textToTransfer, langCode);
        } catch (InvalidArgumentException e) {
            byte[] result = isSuccessfulSpeechResultAgain(textToSpeechClient, audioEncoding, textToTransfer, langCode);
            if (result != null) {
                speechResult = result;
            } else {
                throw new VoiceGenderNotSupportedException("\"Voice isn't supported yet\"");
            }
        } catch (IOException e) {
            throw new RuntimeException("\"The result of text to speech translation wasn't received\"");
        } finally {
            if (textToSpeechClient != null) {
                textToSpeechClient.close();
            }
        }
        if (speechResult == null) {
            throw new RuntimeException("\"The result of text to speech translation wasn't received\"");
        }

        return speechResult;
    }

    private byte[] isSuccessfulSpeechResultAgain(TextToSpeechClient textToSpeechClient, AudioEncoding audioEncoding,
                                                 String textToTransfer, String langCode) {
        byte[] result;
        List<SsmlVoiceGender> genders = new ArrayList<>(List.of(SsmlVoiceGender.FEMALE, SsmlVoiceGender.MALE));
        for (SsmlVoiceGender gender : genders) {
            try {
                result = getSpeechResult(textToSpeechClient, gender, audioEncoding, textToTransfer, langCode);
                return result;
            } catch (InvalidArgumentException e) {
                continue;
            }
        }

        return null;
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
