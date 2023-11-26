package com.nerdtranslator.lingueeapibridge.service.impl;

import com.google.api.gax.core.CredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.language.v1.*;
import com.nerdtranslator.lingueeapibridge.service.AuthenticationDataProvider;
import com.nerdtranslator.lingueeapibridge.service.NaturalLangApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class NaturalLangApiServiceImpl implements NaturalLangApiService {

    private final AuthenticationDataProvider authenticationProvider;

    @Autowired
    public NaturalLangApiServiceImpl(AuthenticationDataProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public String analyzeText(String textToAnalyze) {
        String result = null;
        CredentialsProvider credentialsProvider = () -> {
            try (ByteArrayInputStream keyStream = new ByteArrayInputStream(authenticationProvider.getAuthorizationData())) {
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
            e.printStackTrace();
            throw new RuntimeException("An error in language service response");
        }

        if (result == null || result.isEmpty()) {
            return "undefined";
        }

        return result;
    }
}
