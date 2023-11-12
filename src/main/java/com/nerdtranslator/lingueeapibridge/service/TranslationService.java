package com.nerdtranslator.lingueeapibridge.service;

public interface TranslationService {
    String getTranslation(String originalText, String from, String to);
}
