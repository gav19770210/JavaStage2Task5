package ru.gav19770210.stage2task05.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tpp_ref_account_type", schema = "public", catalog = "stage2task5")
@Getter
@Setter
public class TppRefAccountTypeEntity {
    @Id
    @Column(name = "internal_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "value", unique = true)
    private String value;
}
