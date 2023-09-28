package com.nerdtranslator.lingueeapibridge.service.impl;

import com.nerdtranslator.lingueeapibridge.service.ApiService;
import com.nerdtranslator.lingueeapibridge.service.RedisCacheService;
import com.nerdtranslator.lingueeapibridge.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranslationServiceImpl implements TranslationService {
    private final ApiService apiService;
    private final RedisCacheService cacheService;

    @Override
    public String getTranslation(String originalText, String from, String to) {
        String cachedTranslation = cacheService.getCachedTranslation(originalText, from, to);

        if (cachedTranslation != null) {
            return cachedTranslation;
        } else {
            String translation = apiService.fetchDataFromApi(originalText, from, to);
            cacheService.cacheTranslation(originalText, from, to, translation);
            return translation;
        }
    }
}
