package com.nerdtranslator.lingueeapibridge.service;

import java.util.List;

public interface TranslationApiService {
    String getSingleTranslationFromApi(String originalText, String originalLanguage, String targetLanguage);
    List<String> getTranslationsFromApi(String originalText, String originalLanguage, String targetLanguage);
}
