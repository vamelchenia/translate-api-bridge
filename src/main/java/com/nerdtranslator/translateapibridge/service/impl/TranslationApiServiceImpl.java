package com.nerdtranslator.translateapibridge.service.impl;

import com.google.cloud.translate.v3.*;
import com.nerdtranslator.translateapibridge.exception.GoogleApiResponseException;
import com.nerdtranslator.translateapibridge.service.CredentialsProviderFactory;
import com.nerdtranslator.translateapibridge.service.TranslationApiService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@PropertySource("classpath:sensitive.properties")
@RequiredArgsConstructor
public class TranslationApiServiceImpl implements TranslationApiService {
    private final CredentialsProviderFactory credentialsProviderFactory;
    private static final Logger log = LoggerFactory.getLogger(TranslationApiServiceImpl.class);

    @Override
    public String getSingleTranslationFromApi(String originalText, String originalLanguage, String targetLanguage) {
        try (TranslationServiceClient client = TranslationServiceClient.create(
                TranslationServiceSettings
                        .newBuilder()
                        .setCredentialsProvider(credentialsProviderFactory.getCredentialsProvider())
                        .build())) {
            LocationName parent = LocationName.of(credentialsProviderFactory.getProjectID(), "global");
            TranslateTextRequest request =
                    TranslateTextRequest.newBuilder()
                            .setParent(parent.toString())
                            .setMimeType("text/plain")
                            .setTargetLanguageCode(targetLanguage)
                            .setSourceLanguageCode(originalLanguage)
                            .addContents(originalText)
                            .build();

            TranslateTextResponse response = client.translateText(request);
            String translatedText = response.getTranslations(0).getTranslatedText();
            if (translatedText.isEmpty()) {
                log.error("An error occurred in translation API response: translation is empty");
                throw new GoogleApiResponseException("translation API response: translation is empty");
            }
            return translatedText;
        } catch (IOException e) {
            log.info("An error occurred in translation API response", e);
            throw new GoogleApiResponseException("translation API response: " + e.getMessage());
        }
    }
}