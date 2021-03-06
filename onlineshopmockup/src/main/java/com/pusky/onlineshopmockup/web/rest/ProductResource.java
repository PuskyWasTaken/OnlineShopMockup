package com.pusky.onlineshopmockup.web.rest;

import com.pusky.onlineshopmockup.constants.PuskyConstants;
import com.pusky.onlineshopmockup.domain.Currency;
import com.pusky.onlineshopmockup.domain.Product;
import com.pusky.onlineshopmockup.domain.enumeration.CurrencyKeyList;
import com.pusky.onlineshopmockup.repository.CurrencyRepository;
import com.pusky.onlineshopmockup.repository.ProductRepository;
import com.pusky.onlineshopmockup.service.ProductService;
import com.pusky.onlineshopmockup.util.PaginationUtil;
import com.pusky.onlineshopmockup.util.ResponseUtil;
import com.pusky.onlineshopmockup.util.Translator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
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


    private final ProductRepository productRepository;
    private final ProductService productService;
    private CurrencyRepository currencyRepository;

    public ProductResource(ProductRepository productRepository, ProductService productService, CurrencyRepository currencyRepository) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.currencyRepository = currencyRepository;
    }


//    @GetMapping("/products")
//    public List<Product> getAllProducts() {
//        log.info("REST request to get all Products");
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
        log.info("REST request to get a page of Products");
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

        log.info("REST request to get Product with Product ID : {}", productCode);
        Optional<Product> product = productRepository.findByProductCode(productCode);

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
        log.info("REST request to get Product with Product ID : {}", productCode);
        Optional<Product> product = productRepository.findByProductCode(productCode);

        Optional<BigDecimal> latestPrice = productService.getLatestPriceOfProduct(product).map(bigDecimal -> productService.getLocalisedCurrency(bigDecimal));

        if (latestPrice.isPresent()) {
        }

        /* Only Return the latest price, not the whole Product */
        return ResponseUtil.wrapOrInvalid(latestPrice, Translator.translate("product.invalid"));
    }


}
