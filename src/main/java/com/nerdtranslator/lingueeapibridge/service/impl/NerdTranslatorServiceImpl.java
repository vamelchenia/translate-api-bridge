package com.nerdtranslator.lingueeapibridge.service.impl;

import com.nerdtranslator.lingueeapibridge.service.NerdTranslatorService;
import com.nerdtranslator.lingueeapibridge.service.TranslationApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NerdTranslatorServiceImpl implements NerdTranslatorService {
    private final TranslationApiService translationApiService;

    @Override
    public String getTranslation(String originalText, String originalLanguage, String targetLanguage) {
        List<String> translations =
                translationApiService.fetchDataFromApi("", originalText, originalLanguage, targetLanguage);
        return null;
    }
}

