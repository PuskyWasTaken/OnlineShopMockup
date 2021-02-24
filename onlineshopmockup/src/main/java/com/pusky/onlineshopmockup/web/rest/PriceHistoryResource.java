package com.pusky.onlineshopmockup.web.rest;

import com.pusky.onlineshopmockup.constants.PuskyConstants;
import com.pusky.onlineshopmockup.domain.PriceHistory;
import com.pusky.onlineshopmockup.repository.PriceHistoryRepository;
import com.pusky.onlineshopmockup.util.HeaderUtil;
import com.pusky.onlineshopmockup.util.ResponseUtil;
import com.pusky.onlineshopmockup.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
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
     * {@code POST  /price-histories} : Create a new priceHistory.
     *
     * @param priceHistory the priceHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new priceHistory, or with status {@code 400 (Bad Request)} if the priceHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/price-histories")
    public ResponseEntity<PriceHistory> createPriceHistory(@Valid @RequestBody PriceHistory priceHistory) throws URISyntaxException {
        log.debug("REST request to save PriceHistory : {}", priceHistory);
        if (priceHistory.getId() != null) {
            throw new BadRequestAlertException("A new priceHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PriceHistory result = priceHistoryRepository.save(priceHistory);
        return ResponseEntity.created(new URI("/api/price-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /price-histories} : Updates an existing priceHistory.
     *
     * @param priceHistory the priceHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated priceHistory,
     * or with status {@code 400 (Bad Request)} if the priceHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the priceHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/price-histories")
    public ResponseEntity<PriceHistory> updatePriceHistory(@Valid @RequestBody PriceHistory priceHistory) throws URISyntaxException {
        log.debug("REST request to update PriceHistory : {}", priceHistory);
        if (priceHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PriceHistory result = priceHistoryRepository.save(priceHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, priceHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /price-histories} : get all the priceHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of priceHistories in body.
     */
    @GetMapping("/price-histories")
    public List<PriceHistory> getAllPriceHistories() {
        log.debug("REST request to get all PriceHistories");
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
        log.debug("REST request to get PriceHistory : {}", id);
        Optional<PriceHistory> priceHistory = priceHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(priceHistory);
    }

    /**
     * {@code DELETE  /price-histories/:id} : delete the "id" priceHistory.
     *
     * @param id the id of the priceHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/price-histories/{id}")
    public ResponseEntity<Void> deletePriceHistory(@PathVariable Long id) {
        log.debug("REST request to delete PriceHistory : {}", id);
        priceHistoryRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
