package com.nerdtranslator.lingueeapibridge.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.core.CredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import com.nerdtranslator.lingueeapibridge.service.TextToSpeechService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class TextToSpeechServiceImpl implements TextToSpeechService {

    @Override
    public byte[] transformTextToSoundUsingApi(String textToTransfer, String langCode) {
        return getSpeechFromText(textToTransfer, langCode);
    }

    private static Map<String, String> getCredentials() {
        Properties properties = new Properties();
        Map<String, String> credentials = new LinkedHashMap<>();

        try (FileInputStream inputStream = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        credentials.put("type", properties.getProperty("cred_type"));
        credentials.put("project_id", properties.getProperty("cred_project_id"));
        credentials.put("private_key_id", properties.getProperty("cred_private_key_id"));
        credentials.put("private_key", properties.getProperty("cred_private_key"));
        credentials.put("client_email", properties.getProperty("cred_client_email"));
        credentials.put("client_id", properties.getProperty("cred_client_id"));
        credentials.put("auth_uri", properties.getProperty("cred_auth_uri"));
        credentials.put("token_uri", properties.getProperty("cred_token_uri"));
        credentials.put("auth_provider_x509_cert_url", properties.getProperty("cred_auth_provider_x509_cert_url"));
        credentials.put("client_x509_cert_url", properties.getProperty("cred_client_x509_cert_url"));
        credentials.put("universe_domain", properties.getProperty("cred_universe_domain"));

        return credentials;
    }

    private byte[] getSpeechFromText(String textToTransfer, String langCode) {
        byte[] speechResult;
        String credentialsString;
        try {
            credentialsString = new ObjectMapper().writeValueAsString(getCredentials());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("An error while Json parsing in text to speech service");
        }

        CredentialsProvider credentialsProvider = () -> {
            try (ByteArrayInputStream keyStream = new ByteArrayInputStream(credentialsString.getBytes())) {
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
