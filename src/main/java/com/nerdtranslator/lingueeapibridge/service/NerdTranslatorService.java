package com.nerdtranslator.lingueeapibridge.service;

import com.nerdtranslator.lingueeapibridge.data.TranslationData;

public interface NerdTranslatorService {
    TranslationData getTranslation(String originalText, String originalLanguage, String targetLanguage);
}
