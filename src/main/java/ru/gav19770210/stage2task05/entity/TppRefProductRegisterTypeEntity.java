package ru.gav19770210.stage2task05.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "tpp_ref_product_register_type", schema = "public", catalog = "stage2task5")
@Getter
@Setter
public class TppRefProductRegisterTypeEntity {
    @Id
    @Column(name = "internal_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "value", unique = true)
    private String value;

    @Column(name = "register_type_name")
    private String registerTypeName;

    @Column(name = "product_class_code")
    private String productClassCode;

    @Column(name = "register_type_start_date")
    private Timestamp registerTypeStartDate;

    @Column(name = "register_type_end_date")
    private Timestamp registerTypeEndDate;

    @Column(name = "account_type")
    private String accountType;
}
