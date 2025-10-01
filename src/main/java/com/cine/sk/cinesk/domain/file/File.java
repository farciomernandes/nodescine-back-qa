package com.cine.sk.cinesk.domain.file;

import java.time.Instant;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class File {
    private final String id;
    private final String name;
    private final StorageType storageType;
    private final String version;
    private final String uri;
    private final Instant createDate;
    private final UUID createdBy;

    public File(String id, String name, StorageType storageType, String version, String uri, Instant createDate, UUID createdBy) {
        this.id = id;
        this.name = name;
        this.storageType = storageType;
        this.version = version;
        this.uri = uri;
        this.createDate = createDate;
        this.createdBy = createdBy;
    }

}
