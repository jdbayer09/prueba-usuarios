package com.jdbayer.prueba.api.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"}, name = "user_unique_email")
})
@Getter
@Setter
@ToString
public class UserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 6979800523222990760L;

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", nullable = false, length = 70)
    private String name;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "password", length = 200, nullable = false)
    private String password;

    @Column(name = "created", nullable = false)
    private ZonedDateTime created;

    @Column(name = "modified", nullable = false)
    private ZonedDateTime modified;

    @Column(name = "last_login", nullable = false)
    private ZonedDateTime lastLogin;

    @Column(name = "token", length = 500, nullable = false)
    private String token;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @ToString.Exclude
    private List<PhoneEntity> phones;

    @PrePersist
    private void prePersist() {
        this.setCreated(ZonedDateTime.now());
        this.setModified(ZonedDateTime.now());
        this.setLastLogin(ZonedDateTime.now());
    }
}
