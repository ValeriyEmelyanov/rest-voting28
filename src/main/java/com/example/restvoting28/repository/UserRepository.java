package com.example.restvoting28.repository;

import com.example.restvoting28.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(String email);

    default User getExistedByEmail(String email) {
        return findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email=" + email + " not found"));
    }
}