package com.pusky.onlineshopmockup.service;

import com.pusky.onlineshopmockup.domain.enumeration.ProductState;
import com.pusky.onlineshopmockup.repository.ProductRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableScheduling
public class BatchJobs {

    private static final String[] HEADERS = new String[]{"Id", "ProductCode", "State", "LatestPriceInEUR"};
    private final Logger log = LoggerFactory.getLogger(BatchJobs.class);
    private final ProductRepository productRepository;
    private final ProductService productService;

    public BatchJobs(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    /**
     * At 00:00 every day
     **/
    @Scheduled(cron = "@midnight")
//    @Scheduled(fixedDelay = 1000)
    public void launchJob() throws Exception {

        log.info("Launched export job: ----");

        final String csvFilePath = "VALID_PRODUCTS_" + ZonedDateTime.now().toLocalDate() + ".csv";

        FileWriter out = new FileWriter(csvFilePath);

        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
                .withHeader(HEADERS))) {

            productService.findByProductState(ProductState.VALID).forEach(product -> {
                try {

                    printer.printRecord(
                            product.getId(),
                            product.getProductCode(),
                            product.getState(),
                            productService.getLatestPriceOfProduct(java.util.Optional.of(product)).get());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
