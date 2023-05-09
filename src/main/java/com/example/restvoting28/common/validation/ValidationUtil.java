package com.example.restvoting28.common.validation;

import com.example.restvoting28.common.HasOwner;
import com.example.restvoting28.common.exception.IllegalRequestDataException;
import com.example.restvoting28.common.model.BaseEntity;
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

    public static void assureOwnerIdConsistent(HasOwner entity, long ownerId) {
        if (entity.getOwnerId() == null) {
            entity.setOwnerId(ownerId);
        } else if (entity.ownerId() != ownerId) {
            throw new IllegalRequestDataException(entity + " must be with ownerId=" + ownerId);
        }
    }
}
