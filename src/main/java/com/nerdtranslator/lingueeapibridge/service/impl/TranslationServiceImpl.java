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
        String cachedResponse = cacheService.getCachedResponse(originalText, from, to);

        if (cachedResponse != null) {
            return cachedResponse;
        } else {
            String response = apiService.fetchDataFromApi(originalText, from, to);
            cacheService.cacheResponse(originalText, from, to, response);
            return response;
        }
    }
}
