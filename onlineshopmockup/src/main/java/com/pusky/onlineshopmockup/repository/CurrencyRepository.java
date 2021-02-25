package com.pusky.onlineshopmockup.repository;

import com.pusky.onlineshopmockup.domain.Currency;
import com.pusky.onlineshopmockup.domain.enumeration.CurrencyKeyList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the Currency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findByCurrencyKey(CurrencyKeyList currencyKey);
}
