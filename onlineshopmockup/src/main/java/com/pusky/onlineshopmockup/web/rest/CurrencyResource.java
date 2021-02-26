package com.pusky.onlineshopmockup.web.rest;

import com.pusky.onlineshopmockup.constants.PuskyConstants;
import com.pusky.onlineshopmockup.domain.Currency;
import com.pusky.onlineshopmockup.repository.CurrencyRepository;
import com.pusky.onlineshopmockup.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link Currency}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CurrencyResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyResource.class);

    private static final String ENTITY_NAME = "currency";

    private final CurrencyRepository currencyRepository;

    public CurrencyResource(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    /**
     * {@code GET  /currencies} : get all the currencies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of currencies in body.
     */
    @GetMapping("/currencies")
    public List<Currency> getAllCurrencies() {
        log.info("REST request to get all Currencies");
        return currencyRepository.findAll();
    }

    /**
     * {@code GET  /currencies/:id} : get the "id" currency.
     *
     * @param id the id of the currency to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the currency, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/currencies/{id}")
    public ResponseEntity<Currency> getCurrency(@PathVariable Long id) {
        log.info("REST request to get Currency : {}", id);
        Optional<Currency> currency = currencyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(currency);
    }
}
