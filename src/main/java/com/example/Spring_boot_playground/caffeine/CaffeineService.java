package com.example.Spring_boot_playground.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class CaffeineService {

    private Cache<String, String> cache;

    public CaffeineService() {
        cache = Caffeine.newBuilder()
                .expireAfter(new Expiry<String, String>() {
                    @Override
                    public long expireAfterCreate(String key, String value, long currentTime) {
                        return 86400000000000L; // In nanoseconds. 1,000,000 ns = 1 ms.
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

    public String setCacheWithTTL(String key, String value, long ttlSeconds) {
        System.out.println("Cache before adding: " + cache.asMap());

        cache.policy().expireVariably().ifPresent(expiry -> {
            System.out.println("Applying TTL with expireVariably for key: " + key);
            expiry.put(key, value, ttlSeconds, TimeUnit.SECONDS);

        });

        System.out.println("Cache after adding: " + cache.getIfPresent(key));
        System.out.println("Cache contents: " + cache.asMap());

        return value;
    }

    public String getCacheDataByKey(String key) {
        return cache.getIfPresent(key);
    }

    public String getDataByKey(String key) {
        String cachedValue = getCacheDataByKey(key);
        if (cachedValue == null) {
            // mock: get data from database and add to caffeine
            String mockData = findById(key);
            long ttl = getTTLToNextMidnight();
            cachedValue = setCacheWithTTL(key, mockData, ttl);
        }
        System.out.println("Cache contents for key '" + key + "': " + cachedValue);
        return cachedValue;
    }

    private long getTTLToNextMidnight() {
        String timezone = "Asia/Taipei";
        ZoneId zoneId = ZoneId.of(timezone);
        ZonedDateTime currentTime = ZonedDateTime.now(zoneId);
        return (currentTime.plusDays(1).toLocalDate().atStartOfDay(zoneId).toInstant().toEpochMilli() - currentTime.toInstant().toEpochMilli()) / 1000;
    }

    private String findById(String id) {
        // This is a mock database method
        return switch (id) {
            case "key1" -> "value1";
            case "key2" -> "value2";
            case "key3" -> "value3";
            default -> "";
        };
    }
}
