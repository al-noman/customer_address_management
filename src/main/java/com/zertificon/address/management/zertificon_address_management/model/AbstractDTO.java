package com.zertificon.address.management.zertificon_address_management.model;

import java.io.Serializable;
import java.util.UUID;

public abstract class AbstractDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;

    private UUID version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getVersion() {
        return version;
    }

    public void setVersion(UUID version) {
        this.version = version;
    }
}
