package com.nerdtranslator.lingueeapibridge.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nerdtranslator.lingueeapibridge.service.LanguagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LanguagesServiceImpl implements LanguagesService {
    @Override
    public Map<String, String> getSupportedLanguages() {
        String filePath = "TranslationApiLanguages.json";
        Map<String, String> languageMap;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(new File(filePath));
            languageMap = getLanguageMap(jsonNode);

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
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
