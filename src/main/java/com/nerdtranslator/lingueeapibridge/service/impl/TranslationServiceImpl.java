package com.nerdtranslator.lingueeapibridge.service.impl;

import com.nerdtranslator.lingueeapibridge.service.ApiService;
import com.nerdtranslator.lingueeapibridge.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranslationServiceImpl implements TranslationService {
    private final ApiService apiService;

    @Override
    public String getTranslation(String originalText, String from, String to) {
        String translation = apiService.fetchDataFromApi(originalText, from, to);
        if (translation == null || translation.length() == 0) {
            return "Not found";
        }
        return translation;
    }
}

