package com.example.Kafka_practice.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class CaffeineService {

    private Cache<String, String> cache;

    public CaffeineService() {
        cache = Caffeine.newBuilder()
                .expireAfter(new Expiry<String, String>() {
                    @Override
                    public long expireAfterCreate(String key, String value, long currentTime) {
                        return 100000;
                    }

                    @Override
                    public long expireAfterUpdate(String key, String value, long currentTime, long currentDuration) {
                        return currentDuration;
                    }

                    @Override
                    public long expireAfterRead(String key, String value, long currentTime, long currentDuration) {
                        return currentDuration;
                    }

                })
                .build();
    }

    public void putWithTTL(String key, String value, long ttlSeconds) {
        System.out.println("Cache before adding: " + cache.asMap());

        cache.policy().expireVariably().ifPresent(expiry -> {
            System.out.println("Applying TTL with expireVariably for key: " + key);
            expiry.put(key, value, ttlSeconds, TimeUnit.SECONDS);

        });


        System.out.println("Cache contents - 1: " + cache.asMap());
        System.out.println("Cache after adding: " + cache.getIfPresent(key));
        System.out.println("Cache contents - 2: " + cache.asMap());
    }
    public String get(String key) {
        String value = cache.getIfPresent(key);
        System.out.println("Cache contents for key '" + key + "': " + value);
        return value;
    }
}
