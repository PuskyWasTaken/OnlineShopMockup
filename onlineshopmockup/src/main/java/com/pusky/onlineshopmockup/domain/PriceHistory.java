package com.pusky.onlineshopmockup.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * A PriceHistory.
 */
@Entity
@Table(name = "price_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PriceHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "value", precision = 21, scale = 2, nullable = false)
    private BigDecimal value;

    @NotNull
    @Column(name = "effective_date", nullable = false)
    private ZonedDateTime effectiveDate;

    @ManyToOne
    @JsonIgnoreProperties(value = "priceHistories", allowSetters = true)
    private Currency currency;

    @ManyToOne
    @JsonIgnoreProperties(value = "priceHistories", allowSetters = true)
    private Product product;

    public PriceHistory(@NotNull BigDecimal value, @NotNull ZonedDateTime effectiveDate, Currency currency, Product product) {
        this.value = value;
        this.effectiveDate = effectiveDate;
        this.currency = currency;
        this.product = product;
    }

    public PriceHistory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public PriceHistory value(BigDecimal value) {
        this.value = value;
        return this;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public ZonedDateTime getEffectiveDate() {
        return effectiveDate;
    }

    public PriceHistory effectiveDate(ZonedDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
        return this;
    }

    public void setEffectiveDate(ZonedDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public PriceHistory currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Product getProduct() {
        return product;
    }

    public PriceHistory product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PriceHistory)) {
            return false;
        }
        return id != null && id.equals(((PriceHistory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PriceHistory{" +
            "id=" + getId() +
            ", value=" + getValue() +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            "}";
    }
}
