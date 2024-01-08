package com.nerdtranslator.translateapibridge.service.impl;

import com.google.cloud.language.v1.*;
import com.nerdtranslator.translateapibridge.service.CredentialsProviderFactory;
import com.nerdtranslator.translateapibridge.service.NaturalLangApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NaturalLangApiServiceImpl implements NaturalLangApiService {

    private final CredentialsProviderFactory authenticationProvider;

    @Override
    public String analyzeText(String textToAnalyze) {
        String result = null;
        try (LanguageServiceClient languageService = LanguageServiceClient.create(
                LanguageServiceSettings
                        .newBuilder()
                        .setCredentialsProvider(authenticationProvider.getCredentialsProvider())
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
            throw new RuntimeException("An error in language service response: " + e.getMessage());
        }

        if (result == null || result.isEmpty()) {
            return "undefined";
        }

        return result;
    }
}
