package com.example.restvoting28.restaurant.model;

import com.example.restvoting28.common.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "menu_item",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"dated", "restaurant_id", "dish_id"}, name = "menu_item_dated_restaurant_dish_idx")})
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItem extends BaseEntity {
    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
    @JsonIgnore
    private Restaurant restaurant;

    @Column(name = "restaurant_id", nullable = false)
    @NotNull
    private Long restaurantId;

    @Column(name = "dated", nullable = false, columnDefinition = "date")
    @NotNull
    private LocalDate dated;

    @Nullable
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", insertable = false, updatable = false)
    @JsonIgnore
    private Dish dish;

    @Column(name = "dish_id", nullable = false)
    @NotNull
    private Long dishId;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    @Digits(integer = 10, fraction = 2, message = "Invalid price format")
    @NotNull
    private BigDecimal price;
}
