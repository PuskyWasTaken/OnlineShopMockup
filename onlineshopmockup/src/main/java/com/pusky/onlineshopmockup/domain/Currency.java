package com.pusky.onlineshopmockup.domain;

import com.pusky.onlineshopmockup.domain.enumeration.CurrencyKeyList;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A Currency.
 */
@Entity
@Table(name = "currency")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Currency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency_key", nullable = false, unique = true)
    private CurrencyKeyList currencyKey;

    @NotNull
    @Column(name = "base_exchange_rate", precision = 21, scale = 2, nullable = false)
    private BigDecimal baseExchangeRate;

    public Currency(@NotNull CurrencyKeyList currencyKey, @NotNull BigDecimal baseExchangeRate) {
        this.currencyKey = currencyKey;
        this.baseExchangeRate = baseExchangeRate;
    }

    public Currency() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CurrencyKeyList getCurrencyKey() {
        return currencyKey;
    }

    public Currency currencyKey(CurrencyKeyList currencyKey) {
        this.currencyKey = currencyKey;
        return this;
    }

    public void setCurrencyKey(CurrencyKeyList currencyKey) {
        this.currencyKey = currencyKey;
    }

    public BigDecimal getBaseExchangeRate() {
        return baseExchangeRate;
    }

    public Currency baseExchangeRate(BigDecimal baseExchangeRate) {
        this.baseExchangeRate = baseExchangeRate;
        return this;
    }

    public void setBaseExchangeRate(BigDecimal baseExchangeRate) {
        this.baseExchangeRate = baseExchangeRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Currency)) {
            return false;
        }
        return id != null && id.equals(((Currency) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Currency{" +
            "id=" + getId() +
            ", currencyKey='" + getCurrencyKey() + "'" +
            ", baseExchangeRate=" + getBaseExchangeRate() +
            "}";
    }
}
