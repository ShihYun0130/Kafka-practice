package com.example.Kafka_practice.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
                        return 300000000000L; // In nanoseconds. 1,000,000 ns = 1 ms.
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

    public String setWithTTL(String key, String value, long ttlSeconds) {
        System.out.println("Cache before adding: " + cache.asMap());

        cache.policy().expireVariably().ifPresent(expiry -> {
            System.out.println("Applying TTL with expireVariably for key: " + key);
            expiry.put(key, value, ttlSeconds, TimeUnit.SECONDS);

        });

        System.out.println("Cache after adding: " + cache.getIfPresent(key));
        System.out.println("Cache contents: " + cache.asMap());

        return value;
    }
    public String get(String key) {
        String cachedValue = cache.getIfPresent(key);
        if (cachedValue == null) {
            // get data from database and add to cache
            String mockData = getByKey(key);
            long ttl = getTTL();
            cachedValue = setWithTTL(key, mockData, ttl);
        }
        System.out.println("Cache contents for key '" + key + "': " + cachedValue);
        return cachedValue;
    }

    private long getTTL() {
        String timezone = "Asia/Taipei";
        ZoneId zoneId = ZoneId.of(timezone);
        ZonedDateTime currentTime = ZonedDateTime.now(zoneId);
        return (currentTime.plusDays(1).toLocalDate().atStartOfDay(zoneId).toInstant().toEpochMilli() - currentTime.toInstant().toEpochMilli()) / 1000;
    }

    private String getByKey(String key) {
        // This is a mock database method
        if (key.equals("key1")) {
            return "value1";
        } else if (key.equals("key2")) {
            return "value2";
        } else if (key.equals("key3")) {
            return "value3";
        } else {
            return "";
        }
    }
}
