package microsoftAI.KrishiMitra.krishiApp.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    // You can configure Redis or custom cache managers here later.
    // For now, Spring will use a simple in-memory ConcurrentHashMap, which is perfect for dev.
}