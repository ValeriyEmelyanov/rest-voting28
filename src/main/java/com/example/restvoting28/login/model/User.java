package com.example.restvoting28.login.model;

import com.example.restvoting28.common.model.BaseEntity;
import com.example.restvoting28.common.validation.NoHtml;
import com.example.restvoting28.common.validation.View;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity implements Serializable {

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotEmpty
    @Size(max = 128)
    @NoHtml
    private String email;

    @Column(name = "first_name")
    @Size(max = 128)
    @NoHtml
    private String firstName;

    @Column(name = "last_name")
    @Size(max = 128)
    @NoHtml
    private String lastName;

    @Column(name = "password")
    @NotBlank(groups = {View.OnCreate.class})
    @Size(min = 5, max = 128, groups = {View.OnCreate.class})
    // https://stackoverflow.com/a/12505165/548473
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role"}, name = "user_roles_unique")})
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public void setEmail(String email) {
        this.email = StringUtils.hasText(email) ? email.toLowerCase() : null;
    }

    public void normalize() {
        email = StringUtils.hasText(email) ? email.toLowerCase() : null;
    }
}