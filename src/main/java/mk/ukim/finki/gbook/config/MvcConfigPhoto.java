package mk.ukim.finki.gbook.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfigPhoto implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path authorUploadDir = Paths.get("./src/main/resources/static/img/authors");
        String authorUploadPath = authorUploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("./src/main/resources/static/img/authors")
                .addResourceLocations("file:/" + authorUploadPath + "/");
    }
}