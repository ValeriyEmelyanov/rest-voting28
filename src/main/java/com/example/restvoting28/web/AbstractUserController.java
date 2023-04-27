package com.example.restvoting28.web;

import com.example.restvoting28.exception.IllegalRequestDataException;
import com.example.restvoting28.model.User;
import com.example.restvoting28.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static com.example.restvoting28.config.WebSecurityConfig.PASSWORD_ENCODER;
import static com.example.restvoting28.validation.ValidationUtil.assureIdConsistent;
import static com.example.restvoting28.validation.ValidationUtil.checkNew;

@RequiredArgsConstructor
public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;

    public List<User> getAll() {
        log.info("get users all");
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "lastName", "firstName", "email"));
    }

    public User get(long id) {
        log.info("get user by id={}", id);
        return userRepository.getExisted(id);
    }

    public User getByEmail(String email) {
        log.info("get user by email={}", email);
        return userRepository.getExistedByEmail(email);
    }

    public User create(User user) {
        log.info("create user {}", user);
        checkNew(user);
        return userRepository.prepareAndCreate(user);
    }

    @Transactional
    public User update(User user, long id) {
        log.info("update user {} with id={}", user, id);
        assureIdConsistent(user, id);
        User dbUser = userRepository.getExisted(id);
        return userRepository.prepareAndSave(user, dbUser.getPassword());
    }

    public void delete(long id) {
        log.info("delete user {}", id);
        userRepository.deleteExisted(id);
    }

    @Transactional
    public void changePassword(String oldPassword, String newPassword, long id) {
        log.info("change password for user with id={}", id);
        Assert.isTrue(newPassword.length() >= 5 && newPassword.length() <= 32, "the password size must be in the range from 5 to 32");
        User user = userRepository.getExisted(id);
        if (!PASSWORD_ENCODER.matches(oldPassword, user.getPassword())) {
            throw new IllegalRequestDataException("Wrong old password");
        }
        user.setPassword(PASSWORD_ENCODER.encode(newPassword));
        userRepository.save(user);
    }
}
