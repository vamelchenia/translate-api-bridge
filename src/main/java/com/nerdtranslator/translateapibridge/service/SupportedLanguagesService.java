package com.nerdtranslator.translateapibridge.service;

import java.util.Map;

public interface SupportedLanguagesService {
    Map<String, String> getSupportedLanguagesForTranslationApi();
    Map<String, String> getSupportedLanguagesForNaturalLanguageApi();
}
