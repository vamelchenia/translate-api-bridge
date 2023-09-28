package com.nerdtranslator.lingueeapibridge.service;

public interface RedisCacheService {
    String getCachedResponse(String originalText, String from, String to);
    void cacheResponse(String originalText, String from, String to, String response);
}
