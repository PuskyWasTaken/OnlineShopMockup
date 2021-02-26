package com.pusky.onlineshopmockup.service;

import com.pusky.onlineshopmockup.constants.PuskyConstants;
import com.pusky.onlineshopmockup.domain.Currency;
import com.pusky.onlineshopmockup.domain.PriceHistory;
import com.pusky.onlineshopmockup.domain.Product;
import com.pusky.onlineshopmockup.domain.enumeration.CurrencyKeyList;
import com.pusky.onlineshopmockup.domain.enumeration.ProductState;
import com.pusky.onlineshopmockup.repository.CurrencyRepository;
import com.pusky.onlineshopmockup.repository.ProductRepository;
import com.pusky.onlineshopmockup.util.Translator;
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
    private final CurrencyRepository currencyRepository;


    public ProductService(ProductRepository productRepository, CurrencyRepository currencyRepository) {
        this.productRepository = productRepository;
        this.currencyRepository = currencyRepository;
    }

    public static Optional<PriceHistory> getLatestPriceHistory(Product productObj) {
        return productObj.getPriceHistories().stream().max(Comparator.comparing(PriceHistory::getEffectiveDate));
    }

    public BigDecimal getLocalisedCurrency(BigDecimal latestPrice) {

        final String defaultCurrency = Translator.translate("default.currency");
        CurrencyKeyList currencyKey = CurrencyKeyList.EUR;

        try {
            currencyKey = CurrencyKeyList.valueOf(defaultCurrency);

        } catch (IllegalArgumentException e) {
            log.error("CurrencyKeyList Enum value for string: " + defaultCurrency + " does not exist!");

        } finally {

            if (!currencyKey.equals(CurrencyKeyList.EUR)) {

                /* We have to convert to a new currency */
                log.info("Client currencyKey: " + currencyKey);

                final Currency currency = currencyRepository.findByCurrencyKey(currencyKey);

                /* Price in EUR */
                final BigDecimal convertedPrice = latestPrice.multiply(currency.getBaseExchangeRate()).setScale(PuskyConstants.BIG_DECIMAL_SCALE, RoundingMode.HALF_UP);
                log.info("Client currency is: " + currency);

                return convertedPrice;
            }
        }

        return latestPrice;
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
                    latestPrice = Optional.of((priceHistory.getValue().divide(baseExchangeRate, PuskyConstants.BIG_DECIMAL_SCALE, RoundingMode.HALF_UP)));
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
