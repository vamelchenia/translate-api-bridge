package com.nerdtranslator.lingueeapibridge.service.impl;

import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.Translation;
import com.google.cloud.translate.v3.TranslationServiceClient;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.nerdtranslator.lingueeapibridge.service.TranslationApiService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TranslationApiServiceImpl implements TranslationApiService {
    @Override
    public List<String> fetchDataFromApi(String projectId, String originalText, String originalLanguage, String targetLanguage) {
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            LocationName parent = LocationName.of(projectId, "global");

            TranslateTextRequest request =
                    TranslateTextRequest.newBuilder()
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
}
