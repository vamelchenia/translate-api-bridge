package com.nerdtranslator.lingueeapibridge.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.core.CredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.language.v1.*;
import com.nerdtranslator.lingueeapibridge.service.NaturalLangService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class NaturalLangServiceImpl implements NaturalLangService {

    @Override
    public String analyzeText(String textToAnalyze) {
        return analyzeTextMethod(textToAnalyze);
    }

    private static Map<String, Object> getCredentials() {
        Properties properties = new Properties();
        Map<String, Object> credentials = new LinkedHashMap<>();

        try (FileInputStream inputStream = new FileInputStream("src/main/resources/sensitive.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        credentials.put("type", properties.getProperty("SPEECH_TYPE"));
        credentials.put("project_id", properties.getProperty("SPEECH_PROJECT_ID"));
        credentials.put("private_key_id", properties.getProperty("SPEECH_PRIVATE_KEY_ID"));
        credentials.put("private_key", properties.getProperty("SPEECH_PRIVATE_KEY"));
        credentials.put("client_email", properties.getProperty("SPEECH_CLIENT_EMAIL"));
        credentials.put("client_id", properties.getProperty("SPEECH_CLIENT_ID"));
        credentials.put("auth_uri", properties.getProperty("SPEECH_AUTH_URI"));
        credentials.put("token_uri", properties.getProperty("SPEECH_TOKEN_URI"));
        credentials.put("auth_provider_x509_cert_url", properties.getProperty("SPEECH_AUTH_PROVIDER_X509_CERT_URL"));
        credentials.put("client_x509_cert_url", properties.getProperty("SPEECH_CLIENT_X509_CERT_URL"));
        credentials.put("universe_domain", properties.getProperty("SPEECH_UNIVERSE_DOMAIN"));

        return credentials;
    }

    private String analyzeTextMethod(String textToAnalyze) {
        String result = null;
        String credentialsString;
        try {
            credentialsString = new ObjectMapper().writeValueAsString(getCredentials());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("An error while Json parsing in language service");
        }

        final String finalCredentialsString = credentialsString;
        CredentialsProvider credentialsProvider = () -> {
            try (ByteArrayInputStream keyStream = new ByteArrayInputStream(finalCredentialsString.getBytes())) {
                return ServiceAccountCredentials.fromStream(keyStream);
            }
        };

        try (LanguageServiceClient languageService = LanguageServiceClient.create(
                LanguageServiceSettings
                        .newBuilder()
                        .setCredentialsProvider(credentialsProvider)
                        .build())) {

            Document document = Document
                    .newBuilder()
                    .setContent(textToAnalyze)
                    .setType(Document.Type.PLAIN_TEXT)
                    .build();

            AnalyzeSyntaxResponse response = languageService.analyzeSyntax(document);

            for (Token token : response.getTokensList()) {
                if (token == null || token.getText().getContent().isEmpty()) {
                    throw new RuntimeException("TokenList is empty");
                }
                PartOfSpeech partOfSpeech = token.getPartOfSpeech();
                result = partOfSpeech.getTag().name();
            }

        } catch (IOException e) {
            throw new RuntimeException("An error in language service response");
        }

        if (result == null || result.isEmpty()) {
            return "undefined";
        }
        return result;
    }
}
