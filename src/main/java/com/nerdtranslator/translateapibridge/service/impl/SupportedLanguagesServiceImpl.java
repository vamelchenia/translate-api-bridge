package com.nerdtranslator.translateapibridge.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.translate.v3.*;
import com.nerdtranslator.translateapibridge.exception.GoogleApiResponseException;
import com.nerdtranslator.translateapibridge.service.CredentialsProviderFactory;
import com.nerdtranslator.translateapibridge.service.SupportedLanguagesService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SupportedLanguagesServiceImpl implements SupportedLanguagesService {
    private final CredentialsProviderFactory credentialsProviderFactory;
    private static final Logger log = LoggerFactory.getLogger(SupportedLanguagesServiceImpl.class);

    @Override
    public Map<String, String> getSupportedLanguagesForTranslationApi() {
        try (TranslationServiceClient client = TranslationServiceClient.create(
                TranslationServiceSettings
                        .newBuilder()
                        .setCredentialsProvider(credentialsProviderFactory.getCredentialsProvider())
                        .build())) {
            LocationName parent = LocationName.of(credentialsProviderFactory.getProjectID(), "global");
            GetSupportedLanguagesRequest request =
                    GetSupportedLanguagesRequest.newBuilder()
                            .setParent(parent.toString())
                            .setDisplayLanguageCode("en")
                            .build();
            SupportedLanguages response = client.getSupportedLanguages(request);
            return getLanguageMap(response.getLanguagesList());
        } catch (IOException e) {
            log.error("An error occurred in SupportedLanguagesServiceImp", e);
            throw new GoogleApiResponseException("SupportedLanguagesServiceImpl" + e.getMessage());
        }
    }

    @Override
    public Map<String, String> getSupportedLanguagesForNaturalLanguageApi() {
        String filePath = "data/NaturalLangApiSupportedLanguages.json";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            JsonNode jsonNode = objectMapper.readTree(inputStream);
            return getLanguageMap(jsonNode);
        } catch (IOException e) {
            log.error("An error occurred in SupportedLanguagesServiceImp", e);
            throw new RuntimeException("SupportedLanguagesServiceImp" + e.getMessage());
        }
    }

    private static Map<String, String> getLanguageMap(List<SupportedLanguage> supportedLanguages) {
        Map<String, String> languageMap = new TreeMap<>();
        for (SupportedLanguage language : supportedLanguages) {
            languageMap.put(language.getDisplayName(), language.getLanguageCode());
        }
        return languageMap;
    }

    private static Map<String, String> getLanguageMap(JsonNode jsonNode) {
        Map<String, String> languageMap = new HashMap<>();
        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            languageMap.put(entry.getKey(), entry.getValue().asText());
        }
        return languageMap;
    }
}
