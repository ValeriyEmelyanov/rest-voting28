package com.example.restvoting28.login.dto;

import com.example.restvoting28.common.validation.View;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PasswordRequest {
    @NotBlank(groups = View.Profile.class)
    @JsonView(View.Profile.class)
    private String oldPassword;

    @NotBlank
    @Size(min = 5, max = 128)
    private String newPassword;
}
