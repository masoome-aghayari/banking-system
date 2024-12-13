package ir.azkivaam.banking_system.config;

/*
 * @author masoome.aghayari
 * @since 12/1/24
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageSourceConfig {

    @Bean
    public MessageSource messageSource(@Value("${application.encoding}") String encoding) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding(encoding);
        messageSource.setCacheSeconds(10);
        return messageSource;
    }
}
