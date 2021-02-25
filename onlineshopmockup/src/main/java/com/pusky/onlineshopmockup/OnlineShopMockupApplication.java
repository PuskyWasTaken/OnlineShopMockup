package com.pusky.onlineshopmockup;

import com.pusky.onlineshopmockup.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;


@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(ApplicationProperties.class)
public class OnlineShopMockupApplication {

    private final Logger log = LoggerFactory.getLogger(OnlineShopMockupApplication.class);




    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {

        SpringApplication.run(OnlineShopMockupApplication.class, args);
    }


}
