package com.nerdtranslator.lingueeapibridge.service.impl;

import com.nerdtranslator.lingueeapibridge.service.TextToSpeechService;
import com.nerdtranslator.lingueeapibridge.service.TranslationApiService;
import com.nerdtranslator.lingueeapibridge.service.NerdTranslatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NerdTranslatorServiceImpl implements NerdTranslatorService {
    private final TranslationApiService translationApiService;
    private final TextToSpeechService textToSpeechService;

    @Override
    public String getTranslation(String originalText, String originalLanguage, String targetLanguage) {
        return null;
    }
}

