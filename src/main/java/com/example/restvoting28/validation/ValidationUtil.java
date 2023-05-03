package com.example.restvoting28.validation;

import com.example.restvoting28.exception.IllegalRequestDataException;
import com.example.restvoting28.model.BaseEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(BaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalRequestDataException(entity.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(BaseEntity entity, long id) {
        // http://stackoverflow.com/a/32728226/548473
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.id() != id) {
            throw new IllegalRequestDataException(entity + " must be with id=" + id);
        }
    }
}
