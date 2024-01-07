package com.nerdtranslator.translateapibridge.service;

import com.nerdtranslator.translateapibridge.data.TranslationData;

public interface NerdTranslatorService {
    TranslationData getTranslation(String originalText, String originalLanguage, String targetLanguage);
}
