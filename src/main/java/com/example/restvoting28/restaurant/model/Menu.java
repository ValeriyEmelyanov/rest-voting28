package com.example.restvoting28.restaurant.model;

import com.example.restvoting28.common.HasOwner;
import com.example.restvoting28.common.model.BaseEntity;
import com.example.restvoting28.common.validation.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "dated"}, name = "menu_restaurant_dated_idx")})
@Setter
@Getter
@NoArgsConstructor
public class Menu extends BaseEntity implements HasOwner {
    @Nullable
    @ManyToOne
    @JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
    @JsonIgnore
    private Restaurant restaurant;

    @Column(name = "restaurant_id", nullable = false)
    @NotNull(groups = View.OnCreate.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long restaurantId;

    @Column(name = "dated", nullable = false, columnDefinition = "date")
    @NotNull
    private LocalDate dated;

    @OneToMany(mappedBy = "menu", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    @Fetch(FetchMode.SUBSELECT)
    private List<MenuItem> items;

    @Override
    @JsonIgnore
    public Long getOwnerId() {
        return restaurantId;
    }

    @Override
    public void setOwnerId(Long ownerId) {
        this.restaurantId = ownerId;
    }
}
