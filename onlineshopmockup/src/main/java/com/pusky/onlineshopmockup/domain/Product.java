package com.pusky.onlineshopmockup.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pusky.onlineshopmockup.domain.enumeration.ProductState;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "product_code", nullable = false, unique = true)
    private String productCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private ProductState state;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = "product", allowSetters = true)
    private Set<PriceHistory> priceHistories = new HashSet<>();

    public Product(@NotNull String productCode, @NotNull ProductState state) {
        this.productCode = productCode;
        this.state = state;
    }

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public Product productCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public ProductState getState() {
        return state;
    }

    public Product state(ProductState state) {
        this.state = state;
        return this;
    }

    public void setState(ProductState state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public Product description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public Product image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<PriceHistory> getPriceHistories() {
        return priceHistories;
    }

    public Product priceHistories(Set<PriceHistory> priceHistories) {
        this.priceHistories = priceHistories;
        return this;
    }

    public Product addPriceHistory(PriceHistory priceHistory) {
        this.priceHistories.add(priceHistory);
        priceHistory.setProduct(this);
        return this;
    }

    public Product removePriceHistory(PriceHistory priceHistory) {
        this.priceHistories.remove(priceHistory);
        priceHistory.setProduct(null);
        return this;
    }

    public void setPriceHistories(Set<PriceHistory> priceHistories) {
        this.priceHistories = priceHistories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
                "id=" + getId() +
                ", productCode='" + getProductCode() + "'" +
                ", state='" + getState() + "'" +
                ", description='" + getDescription() + "'" +
                ", image='" + getImage() + "'" +
                "}";
    }
}
