package com.example.restvoting28.common;

import org.springframework.util.Assert;

public interface HasOwner {
    Long getOwnerId();

    void setOwnerId(Long ownerId);

    default long ownerId() {
        Assert.notNull(getOwnerId(), "Entity must have ownerId");
        return getOwnerId();
    }
}
