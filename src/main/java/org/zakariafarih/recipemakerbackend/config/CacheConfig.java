package org.zakariafarih.recipemakerbackend.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    // Configure cache manager if needed (default is sufficient for simple use cases)
}
