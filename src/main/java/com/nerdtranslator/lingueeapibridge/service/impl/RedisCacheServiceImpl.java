package com.nerdtranslator.lingueeapibridge.service.impl;

import com.nerdtranslator.lingueeapibridge.service.RedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisCacheServiceImpl implements RedisCacheService {
    private final RedisTemplate<String, String> redisTemplate;

    @Cacheable(value = "translationCache", key = "{#originalText, #from, #to}")
    @Override
    public String getCachedTranslation(String originalText, String from, String to) {
        String cacheKey = originalText + ":" + from + ":" + to;
        return redisTemplate.opsForValue().get(cacheKey);
    }

    @CacheEvict(value = "translationCache", key = "{#originalText, #from, #to}")
    @Override
    public void cacheTranslation(String originalText, String from, String to, String translation) {
        String cacheKey = originalText + ":" + from + ":" + to;
        redisTemplate.opsForValue().set(cacheKey, translation);
    }
}
