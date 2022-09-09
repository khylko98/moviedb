package ua.khylko.moviedb;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MoviedbApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoviedbApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("MOVIEDB APP")
                .version("v1.0.0")
                .description(   "Rest Api App on the Spring " +
                                "for creating User profiles " +
                                "where users can add watched " +
                                "movies in special list 'watchedMovies'");
    }
}
