package com.example.restvoting28.login;

import com.example.restvoting28.common.BaseRepository;
import com.example.restvoting28.common.exception.NotFoundException;
import com.example.restvoting28.login.model.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.restvoting28.config.WebSecurityConfig.PASSWORD_ENCODER;

@Transactional(readOnly = true)
@CacheConfig(cacheNames = "users")
public interface UserRepository extends BaseRepository<User> {

    @Cacheable(key = "#email")
    @Query("select u from User u where u.email = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(String email);

    @Cacheable(key = "#email")
    default User getExistedByEmail(String email) {
        return findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException("User with email=" + email + " not found"));
    }

    @Transactional
    @CachePut(key = "#user.email")
    default User prepareAndCreate(User user) {
        return prepareAndSave(user, PASSWORD_ENCODER.encode(user.getPassword()));
    }

    @Transactional
    @CachePut(key = "#user.email")
    default User prepareAndSave(User user, String encPassword) {
        user.setPassword(encPassword);
        user.normalize();
        return save(user);
    }

}