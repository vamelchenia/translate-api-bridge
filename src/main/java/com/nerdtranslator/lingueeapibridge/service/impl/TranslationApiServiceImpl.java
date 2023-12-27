package com.nerdtranslator.lingueeapibridge.service.impl;

import com.google.cloud.translate.v3.*;
import com.nerdtranslator.lingueeapibridge.service.CredentialsProviderFactory;
import com.nerdtranslator.lingueeapibridge.service.TranslationApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:sensitive.properties")
@RequiredArgsConstructor
public class TranslationApiServiceImpl implements TranslationApiService {
    private final CredentialsProviderFactory credentialsProviderFactory;
    private final Environment env;

    @Override
    public List<String> getTranslationsFromApi(String originalText, String originalLanguage, String targetLanguage) {
        try (TranslationServiceClient client = TranslationServiceClient.create(
                TranslationServiceSettings
                        .newBuilder()
                        .setCredentialsProvider(credentialsProviderFactory.getCredentialsProvider())
                        .build())) {
            LocationName parent = LocationName.of(env.getProperty("PROJECT_ID"), "global");
            TranslateTextRequest request = TranslateTextRequest.newBuilder()
                    .setParent(parent.toString())
                    .setMimeType("text/plain")
                    .setTargetLanguageCode(targetLanguage)
                    .addContents(originalText)
                    .build();

            TranslateTextResponse response = client.translateText(request);
            return response.getTranslationsList()
                    .stream()
                    .map(Translation::getTranslatedText)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String getSingleTranslationFromApi(String originalText, String originalLanguage, String targetLanguage) {
        try (TranslationServiceClient client = TranslationServiceClient.create(
                TranslationServiceSettings
                        .newBuilder()
                        .setCredentialsProvider(credentialsProviderFactory.getCredentialsProvider())
                        .build())) {
            LocationName parent = LocationName.of(env.getProperty("PROJECT_ID"), "global");
            TranslateTextRequest request =
                    TranslateTextRequest.newBuilder()
                            .setParent(parent.toString())
                            .setMimeType("text/plain")
                            .setTargetLanguageCode(targetLanguage)
                            .addContents(originalText)
                            .build();

            TranslateTextResponse response = client.translateText(request);
            return response.getTranslations(0).getTranslatedText();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
