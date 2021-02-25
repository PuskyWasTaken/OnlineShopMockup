package com.pusky.onlineshopmockup.web.rest;

import com.pusky.onlineshopmockup.constants.PuskyConstants;
import com.pusky.onlineshopmockup.domain.PriceHistory;
import com.pusky.onlineshopmockup.repository.PriceHistoryRepository;
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
 * REST controller for managing {@link PriceHistory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PriceHistoryResource {

    private final Logger log = LoggerFactory.getLogger(PriceHistoryResource.class);

    private static final String ENTITY_NAME = "priceHistory";

    private final String applicationName = PuskyConstants.CLIENT_APP_NAME;

    private final PriceHistoryRepository priceHistoryRepository;

    public PriceHistoryResource(PriceHistoryRepository priceHistoryRepository) {
        this.priceHistoryRepository = priceHistoryRepository;
    }

    /**
     * {@code GET  /price-histories} : get all the priceHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of priceHistories in body.
     */
    @GetMapping("/price-histories")
    public List<PriceHistory> getAllPriceHistories() {
        log.info("REST request to get all PriceHistories");
        return priceHistoryRepository.findAll();
    }

    /**
     * {@code GET  /price-histories/:id} : get the "id" priceHistory.
     *
     * @param id the id of the priceHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the priceHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/price-histories/{id}")
    public ResponseEntity<PriceHistory> getPriceHistory(@PathVariable Long id) {
        log.info("REST request to get PriceHistory : {}", id);
        Optional<PriceHistory> priceHistory = priceHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(priceHistory);
    }
}
