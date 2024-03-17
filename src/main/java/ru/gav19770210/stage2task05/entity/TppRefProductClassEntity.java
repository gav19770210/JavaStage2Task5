package ru.gav19770210.stage2task05.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tpp_ref_product_class", schema = "public", catalog = "stage2task5")
@Getter
@Setter
public class TppRefProductClassEntity {
    @Id
    @Column(name = "internal_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "value", unique = true)
    private String value;

    @Column(name = "gbl_code")
    private String gblCode;

    @Column(name = "gbl_name")
    private String gblName;

    @Column(name = "product_row_code")
    private String productRowCode;

    @Column(name = "product_row_name")
    private String productRowName;

    @Column(name = "subclass_code")
    private String subclassCode;

    @Column(name = "subclass_name")
    private String subclassName;
}
