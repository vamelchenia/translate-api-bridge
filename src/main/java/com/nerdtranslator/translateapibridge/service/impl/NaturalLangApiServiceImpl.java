package com.nerdtranslator.translateapibridge.service.impl;

import com.google.cloud.language.v1.*;
import com.nerdtranslator.translateapibridge.exception.GoogleApiResponseException;
import com.nerdtranslator.translateapibridge.service.CredentialsProviderFactory;
import com.nerdtranslator.translateapibridge.service.NaturalLangApiService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NaturalLangApiServiceImpl implements NaturalLangApiService {

    private final CredentialsProviderFactory authenticationProvider;
    private static final Logger log = LoggerFactory.getLogger(NaturalLangApiServiceImpl.class);

    @Override
    public String analyzeText(String textToAnalyze, String textLanguage) {
        log.info("NaturalLangApiServiceImpl analyzeText start");
        String result = null;
        try (LanguageServiceClient languageService = getLanguageService()) {
            Document document = getDocument(textToAnalyze, textLanguage);
            AnalyzeSyntaxResponse response = languageService.analyzeSyntax(document);
            for (Token token : response.getTokensList()) {
                if (token == null || token.getText().getContent().isEmpty()) {
                    log.error("An error occurred in natural language API response: Token is empty");
                    throw new GoogleApiResponseException("natural language API response: Token is empty");
                }
                PartOfSpeech partOfSpeech = token.getPartOfSpeech();
                result = partOfSpeech.getTag().name();
            }
        } catch (IOException e) {
            log.error("An error occurred in natural language API response", e);
            throw new GoogleApiResponseException("natural language API response: " + e.getMessage());
        }
        log.info("NaturalLangApiServiceImpl analyzeText end");
        return result;
    }

    private LanguageServiceClient getLanguageService() throws IOException {
        return LanguageServiceClient.create(
                LanguageServiceSettings
                        .newBuilder()
                        .setCredentialsProvider(authenticationProvider.getCredentialsProvider())
                        .build());
    }

    private static Document getDocument(String textToAnalyze, String textLanguage) {
        List<String> supportedLanguages = List.of(
                "zh", "zh-Hant", "en", "fr", "de", "it", "ja", "ko", "pt", "ru", "es");
        Document document;
        if (supportedLanguages.contains(textLanguage)) {
            document = Document
                    .newBuilder()
                    .setContent(textToAnalyze)
                    .setLanguage(textLanguage)
                    .setType(Document.Type.PLAIN_TEXT)
                    .build();
        } else {
            document = Document
                    .newBuilder()
                    .setContent(textToAnalyze)
                    .setType(Document.Type.PLAIN_TEXT)
                    .build();
        }
        return document;
    }
}