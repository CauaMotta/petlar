package br.com.ocauamotta.PetLar.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("uploads/animals");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/public/animals/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}
