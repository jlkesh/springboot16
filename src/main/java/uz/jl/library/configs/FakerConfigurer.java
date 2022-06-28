package uz.jl.library.configs;

import com.github.javafaker.Faker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class FakerConfigurer {

    @Bean
    @Scope("singleton")
    public Faker faker() {
        return new Faker();
    }
}
