package com.nerdtranslator.translateapibridge.service;

public interface TranslationApiService {
    String getSingleTranslationFromApi(String originalText, String originalLanguage, String targetLanguage);
}
