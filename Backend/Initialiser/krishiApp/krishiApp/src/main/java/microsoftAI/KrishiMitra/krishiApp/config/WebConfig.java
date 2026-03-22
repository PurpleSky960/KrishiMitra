package microsoftAI.KrishiMitra.krishiApp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // This opens the gates for the frontend to hit ANY of your API endpoints
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000") // Your teammate's local Node server port
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // This makes your local 'uploads' folder visible to the web.
        // So if an image is saved at C:/.../uploads/leaf.jpg,
        // the frontend can load it via http://localhost:8080/uploads/leaf.jpg
        String uploadPath = "file:" + System.getProperty("user.dir") + "/uploads/";

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }
}