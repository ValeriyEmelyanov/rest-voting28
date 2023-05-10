package com.example.restvoting28.restaurant.model;

import com.example.restvoting28.common.model.BaseEntity;
import com.example.restvoting28.common.validation.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;

@Entity
@Table(name = "menu_items")
@Setter
@Getter
@NoArgsConstructor
public class MenuItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    @JsonIgnore
    private Menu menu;

    @Nullable
    @OneToOne
    @JoinColumn(name = "dish_id", insertable = false, updatable = false)
    @JsonIgnore
    private Dish dish;

    @Column(name = "dish_id", nullable = false)
    @NotNull(groups = View.OnCreate.class)
    private Long dishId;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    @Digits(integer = 10, fraction = 2, message = "Invalid price format")
    @NotNull
    private BigDecimal price;
}
