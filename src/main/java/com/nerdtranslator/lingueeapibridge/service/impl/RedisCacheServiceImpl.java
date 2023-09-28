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

    @Cacheable(value = "apiCache", key = "{#endpoint, #resource}")
    @Override
    public String getCachedResponse(String originalText, String from, String to) {
        String cacheKey = originalText + ":" + from + ":" + to;
        return redisTemplate.opsForValue().get(cacheKey);
    }

    @CacheEvict(value = "apiCache", key = "{#endpoint, #resource}")
    @Override
    public void cacheResponse(String originalText, String from, String to, String response) {
        String cacheKey = originalText + ":" + from + ":" + to;
        redisTemplate.opsForValue().set(cacheKey, response);
    }
}
