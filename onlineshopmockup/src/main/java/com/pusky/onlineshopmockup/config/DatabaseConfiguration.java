package com.pusky.onlineshopmockup.config;

import com.pusky.onlineshopmockup.constants.PuskyConstants;
import com.pusky.onlineshopmockup.domain.Currency;
import com.pusky.onlineshopmockup.domain.PriceHistory;
import com.pusky.onlineshopmockup.domain.Product;
import com.pusky.onlineshopmockup.domain.enumeration.ProductState;
import com.pusky.onlineshopmockup.repository.CurrencyRepository;
import com.pusky.onlineshopmockup.repository.PriceHistoryRepository;
import com.pusky.onlineshopmockup.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class DatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Bean
    CommandLineRunner initDatabase(CurrencyRepository currencyRepository, ProductRepository productRepository, PriceHistoryRepository priceHistoryRepository) {

        return args -> {

            // TODO: Maybe fully implement flags in order to know if we should generate fake data or not/ create default currency entries or not..so on

            /* Create default currencies */
//            createMockData(currencyRepository, productRepository, priceHistoryRepository);

            /* Create default products */
//            createProducts(productRepository);


            /* Add default price history for products */
        };
    }

    private void createMockData(CurrencyRepository currencyRepository, ProductRepository productRepository, PriceHistoryRepository priceHistoryRepository) {


        Set<Currency> currencies = new HashSet<>();

        PuskyConstants.CURRENCY_KEY_DECIMAL_MAP.forEach((currencyKeyList, bigDecimal) -> {

            /* Save the currency */
            final Currency currency = currencyRepository.save(new Currency(currencyKeyList, bigDecimal));
            log.info("Loading Default Currency: " + currency);
            currencies.add(currency);
        });

        AtomicInteger i = new AtomicInteger();
        for (int j = 0; j < 100000; j++) {


            AtomicInteger k = new AtomicInteger();
            currencies.forEach(currency -> {

                /* Save the product */
                final Product product = productRepository.save(new Product(i.toString(), ProductState.VALID));
                log.info("Loading Default Product: " + product);

                /* Generate a price history */
                Set<PriceHistory> priceHistory = new HashSet<>();
                priceHistory.add(new PriceHistory(new BigDecimal(i.incrementAndGet()), ZonedDateTime.now(), currency, product)); // Latest known price
                priceHistory.add(new PriceHistory(new BigDecimal(k.incrementAndGet()), ZonedDateTime.now().minusDays(1), currency, product)); // Latest known price

                log.info("Loading Default Price History: " + priceHistoryRepository.saveAll(priceHistory));

            });
        }

    }
}