package com.nerdtranslator.lingueeapibridge.service;

public interface RedisCacheService {
    String getCachedTranslation(String originalText, String from, String to);
    void cacheTranslation(String originalText, String from, String to, String response);
}
