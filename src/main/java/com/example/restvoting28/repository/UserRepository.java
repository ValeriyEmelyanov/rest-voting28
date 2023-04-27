package com.example.restvoting28.repository;

import com.example.restvoting28.exception.NotFoundException;
import com.example.restvoting28.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.restvoting28.config.WebSecurityConfig.PASSWORD_ENCODER;

@Transactional(readOnly = true)
public interface UserRepository extends BaseRepository<User> {

    @Query("SELECT u FROM User u WHERE u.email = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(String email);

    default User getExistedByEmail(String email) {
        return findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException("User with email=" + email + " not found"));
    }

    @Transactional
    default User prepareAndCreate(User user) {
        return prepareAndSave(user, PASSWORD_ENCODER.encode(user.getPassword()));
    }

    @Transactional
    default User prepareAndSave(User user, String encPassword) {
        user.setPassword(encPassword);
        user.normalize();
        return save(user);
    }

}