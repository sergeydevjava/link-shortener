package org.sergeydevjava.model;


import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static org.sergeydevjava.util.Constant.DEFAULT_DB_USER;

@Getter
@Setter
@MappedSuperclass
public class AuditableEntity {


    private LocalDateTime createTime;
    private String createUser;
    private LocalDateTime lastUpdateTime;
    private String lastUpdateUser;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createTime = now;
        this.createUser = DEFAULT_DB_USER;
        this.lastUpdateTime = now;
        this.lastUpdateUser = DEFAULT_DB_USER;
    }
    @PreUpdate
    public void preUpdate() {
        LocalDateTime now = LocalDateTime.now();
        this.lastUpdateTime = now;
        this.lastUpdateUser = DEFAULT_DB_USER;
    }
}
