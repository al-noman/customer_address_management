package com.zertificon.address.management.zertificon_address_management.model;

import java.io.Serializable;
import java.util.UUID;

public abstract class AbstractDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;

    private int version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
