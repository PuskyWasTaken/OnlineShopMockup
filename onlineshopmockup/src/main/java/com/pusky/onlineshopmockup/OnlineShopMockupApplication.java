package com.pusky.onlineshopmockup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
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
