package com.example.restvoting28.voting.model;

import com.example.restvoting28.common.model.BaseEntity;
import com.example.restvoting28.login.model.User;
import com.example.restvoting28.restaurant.model.Restaurant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Entity
@Table(name = "vote",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "dated"}, name = "vote_user_dated_idx")},
        indexes = {
                @Index(columnList = "dated, restaurant_id", name = "vote_dated_restaurant_idx"),
                @Index(columnList = "dated", name = "vote_dated_idx")
        })
@Setter
@Getter
@NoArgsConstructor
public class Vote extends BaseEntity {
    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private User user;

    @Column(name = "user_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long userId;

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

    public Vote(Long userId, Long restaurantId, LocalDate dated) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.dated = dated;
    }
}
