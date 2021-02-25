package com.pusky.onlineshopmockup.service;

import com.pusky.onlineshopmockup.domain.PriceHistory;
import com.pusky.onlineshopmockup.domain.Product;
import com.pusky.onlineshopmockup.domain.enumeration.CurrencyKeyList;
import com.pusky.onlineshopmockup.domain.enumeration.ProductState;
import com.pusky.onlineshopmockup.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public static Optional<PriceHistory> getLatestPriceHistory(Product productObj) {
        return productObj.getPriceHistories().stream().max(Comparator.comparing(PriceHistory::getEffectiveDate));
    }

    public Optional<BigDecimal> getLatestPriceOfProduct(Optional<Product> product) {

        Optional<BigDecimal> latestPrice = Optional.empty();

        if (product.isPresent() && product.get().getState().equals(ProductState.VALID)) {

            final Optional<PriceHistory> latestPriceHistory = getLatestPriceHistory(product.get());

            latestPrice = Optional.of(latestPriceHistory.get().getValue());

            /* Get the latest price  */
            if (latestPriceHistory.isPresent()) {

                final PriceHistory priceHistory = latestPriceHistory.get();
                final BigDecimal baseExchangeRate = priceHistory.getCurrency().getBaseExchangeRate();

                /* Is the exchange rate different that the base one? */
                if (!priceHistory.getCurrency().getCurrencyKey().equals(CurrencyKeyList.EUR))
                    latestPrice = Optional.of((priceHistory.getValue().divide(baseExchangeRate, 2, RoundingMode.HALF_UP)));
            }
        }

        return latestPrice;
    }

    static Specification<Product> hasProductState(ProductState productState) {
        return (product, cq, cb) -> cb.equal(product.get("state"), productState);
    }

    /**
     * Return a {@link List} of {@link Product} which matches the productState from the database.
     *
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Product> findByProductState(ProductState productState) {

        log.info("Find by productState : {}", productState);
        return productRepository.findAll(Specification.where(hasProductState(productState)));
    }
}
