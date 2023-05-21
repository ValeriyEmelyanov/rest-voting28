package com.example.restvoting28.voting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GuestCountResponse {
    private long restaurantId;
    private String restaurantName;
    private long voteCount;
}
