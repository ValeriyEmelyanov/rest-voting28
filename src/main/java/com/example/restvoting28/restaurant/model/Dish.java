package com.example.restvoting28.restaurant.model;

import com.example.restvoting28.common.HasOwner;
import com.example.restvoting28.common.model.BaseEntity;
import com.example.restvoting28.common.validation.NoHtml;
import com.example.restvoting28.common.validation.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "dish", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "name"}, name = "dish_restaurant_name_idx")})
@Setter
@Getter
@NoArgsConstructor
public class Dish extends BaseEntity implements HasOwner {
    @Column(name = "name", nullable = false)
    @NotBlank
    @NoHtml
    @Size(min = 1, max = 255)
    private String name;

    // https://stackoverflow.com/a/44539145/548473
    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
    @JsonIgnore
    private Restaurant restaurant;

    @Column(name = "restaurant_id", nullable = false)
    @NotNull(groups = View.OnCreate.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long restaurantId;

    @JsonIgnore
    @Override
    public Long getOwnerId() {
        return restaurantId;
    }

    @Override
    public void setOwnerId(Long ownerId) {
        this.restaurantId = ownerId;
    }
}
