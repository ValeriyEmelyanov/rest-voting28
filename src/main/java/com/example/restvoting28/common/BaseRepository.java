package com.example.restvoting28.common;

import com.example.restvoting28.common.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

// https://stackoverflow.com/questions/42781264/multiple-base-repositories-in-spring-data-jpa
@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Long> {

    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query.spel-expressions
    @Transactional
    @Modifying
    @Query("delete from #{#entityName} e where e.id=:id")
    int delete(long id);

    // https://stackoverflow.com/a/60695301/548473 (existed delete code 204, not existed: 404)
    default void deleteExisted(long id) {
        if (delete(id) == 0) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
    }

    default T getExisted(long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
    }
}