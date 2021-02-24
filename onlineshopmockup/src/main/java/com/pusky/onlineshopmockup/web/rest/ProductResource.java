package com.pusky.onlineshopmockup.web.rest;

import com.pusky.onlineshopmockup.constants.PuskyConstants;
import com.pusky.onlineshopmockup.domain.PriceHistory;
import com.pusky.onlineshopmockup.domain.Product;
import com.pusky.onlineshopmockup.domain.enumeration.CurrencyKeyList;
import com.pusky.onlineshopmockup.domain.enumeration.ProductState;
import com.pusky.onlineshopmockup.repository.ProductRepository;
import com.pusky.onlineshopmockup.util.HeaderUtil;
import com.pusky.onlineshopmockup.util.PaginationUtil;
import com.pusky.onlineshopmockup.util.ResponseUtil;
import com.pusky.onlineshopmockup.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link Product}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private static final String ENTITY_NAME = "product";

    private final String applicationName = PuskyConstants.CLIENT_APP_NAME;

    private final ProductRepository productRepository;

    public ProductResource(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private static Optional<PriceHistory> getLatestPriceHistory(Product productObj) {
        return productObj.getPriceHistories().stream().max(Comparator.comparing(PriceHistory::getEffectiveDate));
    }

    /**
     * {@code POST  /products} : Create a new product.
     *
     * @param product the product to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new product, or with status {@code 400 (Bad Request)} if the product has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) throws URISyntaxException {
        log.debug("REST request to save Product : {}", product);
        if (product.getId() != null) {
            throw new BadRequestAlertException("A new product cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Product result = productRepository.save(product);
        return ResponseEntity.created(new URI("/api/products/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /products} : Updates an existing product.
     *
     * @param product the product to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated product,
     * or with status {@code 400 (Bad Request)} if the product is not valid,
     * or with status {@code 500 (Internal Server Error)} if the product couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/products")
    public ResponseEntity<Product> updateProduct(@Valid @RequestBody Product product) throws URISyntaxException {
        log.debug("REST request to update Product : {}", product);
        if (product.getId() == null) {
            throw new BadRequestAlertException("Invalid productCode", ENTITY_NAME, "idnull");
        }
        Product result = productRepository.save(product);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, product.getId().toString()))
                .body(result);
    }


//    @GetMapping("/products")
//    public List<Product> getAllProducts() {
//        log.debug("REST request to get all Products");
//        return productRepository.findAll();
//    }

    /**
     * {@code GET  /products} : get all the products.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of products in body.
     */
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(Pageable pageable) {
        log.debug("REST request to get a page of Products");
        Page<Product> page = productRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /products/productCode/:productCode} : get the "productCode" product.
     *
     * @param productCode the productCode of the product to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the product, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/products/productCode/{productCode}")
    public ResponseEntity<Product> getProductById(@PathVariable String productCode) {

        log.debug("REST request to get Product with Product ID : {}", productCode);
        Optional<Product> product = productRepository.findByProductCode(productCode);

        /* Only Return the latest price, not the whole Product */
        final ResponseEntity<Product> responseEntity = ResponseUtil.wrapOrNotFound(product);

        return responseEntity;
    }


    /**
     * {@code GET  /products/:productCode} : get the "productCode" product.
     *
     * @param productCode the productCode of the product to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the product, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/products/{productCode}")
    public ResponseEntity<BigDecimal> getProductByProductCode(@PathVariable String productCode) {
        log.debug("REST request to get Product with Product ID : {}", productCode);
        Optional<Product> product = productRepository.findByProductCode(productCode);
        Optional<BigDecimal> latestPrice = Optional.empty();

        if (product.isPresent() && product.get().getState().equals(ProductState.VALID)) {

            final Optional<PriceHistory> latestPriceHistory = ProductResource.getLatestPriceHistory(product.get());

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

        /* Only Return the latest price, not the whole Product */
        final ResponseEntity<BigDecimal> responseEntity = ResponseUtil.wrapOrInvalid(latestPrice);

        return responseEntity;
    }

    /**
     * {@code DELETE  /products/:productCode} : delete the "productCode" product.
     *
     * @param productCode the productCode of the product to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/products/{productCode}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productCode) {
        log.debug("REST request to delete Product : {}", productCode);
        productRepository.deleteByProductCode(productCode);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, productCode.toString())).build();
    }
}
