package kr.co.goalkeeper.api.config.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new OAuthTypeConverter());
        registry.addConverter(new GoalStateConverter());
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:3000","https://rad-mousse-4487b7.netlify.app")
                .allowCredentials(true)
                .allowedMethods("*");
    }
}
