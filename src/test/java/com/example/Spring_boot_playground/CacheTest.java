package com.example.Spring_boot_playground;

import com.example.Spring_boot_playground.caffeine.CaffeineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;


@SpringBootTest
public class CacheTest {
    @Autowired
    private CaffeineService caffeineService;

    @Test
    void testCacheExpiration() throws InterruptedException {
        String value = caffeineService.setCacheWithTTL("key1", "value1", 3);

        assertThat(value).isEqualTo("value1");

        TimeUnit.SECONDS.sleep(5);

        assertThat(caffeineService.getCacheDataByKey("key1")).isNull();
    }

    @Test
    void testCacheUntilMidnight(){
        String value = caffeineService.getDataByKey("key1");
        assertThat(value).isEqualTo("value1");
    }


}
