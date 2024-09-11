package com.jdbayer.prueba.api.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "phones")
@Getter
@Setter
@ToString
public class PhoneEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 4416138609522590153L;

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "number",nullable = false)
    private long number;

    @Column(name = "citycode", nullable = false)
    private int cityCode;

    @Column(name = "contrycode", nullable = false)
    private int countryCode;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "phone_user_id_fk"))
    private UserEntity user;
}
