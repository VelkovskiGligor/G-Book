package mk.ukim.finki.gbook.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path bookUploadDir = Paths.get("./src/main/resources/static/img/books");
        String bookUploadPath = bookUploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("./src/main/resources/static/img/books")
                .addResourceLocations("file:/" + bookUploadPath + "/");
    }
}