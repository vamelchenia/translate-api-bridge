package com.nerdtranslator.lingueeapibridge.service;

public interface NerdTranslatorService {
    String getTranslation(String originalText, String originalLanguage, String targetLanguage);
}
