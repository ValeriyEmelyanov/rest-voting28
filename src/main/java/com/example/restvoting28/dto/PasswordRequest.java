package com.example.restvoting28.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PasswordRequest {
    @Size(max = 256)
    private String oldPassword;

    @Size(max = 256)
    private String newPassword;
}
