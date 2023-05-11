package com.example.restvoting28.voting.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class VoteRequest {
    @NotNull
    Long restaurantId;
}
