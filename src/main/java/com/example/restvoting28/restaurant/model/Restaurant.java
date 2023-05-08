package com.example.restvoting28.restaurant.model;

import com.example.restvoting28.common.model.BaseEntity;
import com.example.restvoting28.common.validation.NoHtml;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "restaurant_name_idx")})
@Setter
@Getter
@NoArgsConstructor
public class Restaurant extends BaseEntity {
    @Column(name = "name", nullable = false, unique = true)
    @NotBlank
    @NoHtml
    @Size(min = 1, max = 255)
    private String name;

    public Restaurant(Long id, String name) {
        super(id);
        this.name = name;
    }
}
