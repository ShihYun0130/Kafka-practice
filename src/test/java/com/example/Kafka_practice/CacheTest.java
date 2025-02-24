package com.example.Kafka_practice;

import com.example.Kafka_practice.cache.CaffeineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;


@SpringBootTest
public class CacheTest {
    @Autowired
    private CaffeineService caffeineService;


    @Test
    void testCacheExpiration() throws InterruptedException {
        caffeineService.putWithTTL("key1", "value1", 3);

        String value = caffeineService.get("key1");

        assertThat(value).isEqualTo("value1");

        TimeUnit.SECONDS.sleep(5);

        assertThat(caffeineService.get("key1")).isNull();
    }


}
