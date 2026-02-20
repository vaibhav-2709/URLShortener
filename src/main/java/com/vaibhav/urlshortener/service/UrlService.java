package com.vaibhav.urlshortener.service;


import com.vaibhav.urlshortener.entity.Url;
import com.vaibhav.urlshortener.exception.UrlNotFoundException;
import com.vaibhav.urlshortener.repository.UrlRepository;
import com.vaibhav.urlshortener.util.Base62Encoder;
import com.vaibhav.urlshortener.exception.RateLimitExceededException;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public UrlService(UrlRepository urlRepository,
                      RedisTemplate<String, String> redisTemplate) {
        this.urlRepository = urlRepository;
        this.redisTemplate = redisTemplate;
    }

    // 🔥 Create Short URL using Base62 encoding
    public Url createShortUrl(String originalUrl) {

        originalUrl = originalUrl.trim();

        // Step 1: Save URL without shortCode
        Url url = Url.builder()
                .originalUrl(originalUrl)
                .createdAt(LocalDateTime.now())
                .clickCount(0L)
                .build();

        Url savedUrl = urlRepository.save(url);

        // Step 2: Generate Base62 using DB ID
        String shortCode = Base62Encoder.encode(savedUrl.getId());

        savedUrl.setShortCode(shortCode);

        return urlRepository.save(savedUrl);
    }

    // 🔥 Cache-first redirect logic
    public Url getAndIncrement(String shortCode) {

        // 1️⃣ Check Redis first
        String cachedUrl = redisTemplate.opsForValue().get(shortCode);

        if (cachedUrl != null) {

            Url url = urlRepository.findByShortCode(shortCode)
                    .orElseThrow(() ->
                            new UrlNotFoundException("Short URL not found"));

            url.setClickCount(url.getClickCount() + 1);
            urlRepository.save(url);

            url.setOriginalUrl(cachedUrl);
            return url;
        }

        // 2️⃣ If not in cache → fetch from DB
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new UrlNotFoundException("Short URL not found"));

        url.setClickCount(url.getClickCount() + 1);
        urlRepository.save(url);

        // 3️⃣ Store in Redis with TTL (10 minutes)
        redisTemplate.opsForValue().set(
                shortCode,
                url.getOriginalUrl(),
                10,
                TimeUnit.MINUTES
        );

        return url;
    }
    public void checkRateLimit(HttpServletRequest request) {

    String clientIp = request.getRemoteAddr();
    String key = "rate_limit:" + clientIp;

    String currentCount = redisTemplate.opsForValue().get(key);

    if (currentCount != null && Integer.parseInt(currentCount) >= 5) {
        throw new RateLimitExceededException("Rate limit exceeded. Try again later.");
    }

    redisTemplate.opsForValue().increment(key);

    if (currentCount == null) {
        redisTemplate.expire(key, 1, TimeUnit.MINUTES);
    }
}
}
