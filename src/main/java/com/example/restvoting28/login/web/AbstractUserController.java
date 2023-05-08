package com.example.restvoting28.login.web;

import com.example.restvoting28.login.model.User;
import com.example.restvoting28.login.UserRepository;
import com.example.restvoting28.common.validation.UniqueMailValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.restvoting28.config.WebSecurityConfig.PASSWORD_ENCODER;
import static com.example.restvoting28.common.validation.ValidationUtil.assureIdConsistent;
import static com.example.restvoting28.common.validation.ValidationUtil.checkNew;

@RequiredArgsConstructor
public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    @Autowired
    private UniqueMailValidator uniqueMailValidator;

    @InitBinder("user")
    protected void initUserBiding(WebDataBinder binder) {
        binder.addValidators(uniqueMailValidator);
    }

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
    public void changePassword(String newPassword, long id) {
        log.info("change password for user with id={}", id);
        User user = userRepository.getExisted(id);
        user.setPassword(PASSWORD_ENCODER.encode(newPassword));
        userRepository.save(user);
    }
}
