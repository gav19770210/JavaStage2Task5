package ru.gav19770210.stage2task05.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tpp_product", schema = "public", catalog = "stage2task5")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TppProductEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "product_code_id", referencedColumnName = "internal_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TppRefProductClassEntity productCodeId;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "type")
    private String type;

    @Column(name = "number")
    private String number;

    @Column(name = "priority")
    private Long priority;

    @Column(name = "date_of_conclusion")
    private Date dateOfConclusion;

    @Column(name = "start_date_time")
    private Date startDateTime;

    @Column(name = "end_date_time")
    private Date endDateTime;

    @Column(name = "days")
    private Long days;

    @Column(name = "penalty_rate")
    private BigDecimal penaltyRate;

    @Column(name = "nso")
    private BigDecimal nso;

    @Column(name = "threshold_amount")
    private BigDecimal thresholdAmount;

    @Column(name = "interest_rate_type")
    private String interestRateType;

    @Column(name = "tax_rate")
    private BigDecimal taxRate;

    @Column(name = "reason_close")
    private String reasonClose;

    @Column(name = "state")
    private String state;

    @OneToMany(mappedBy = "productId", fetch = FetchType.LAZY)
    private List<AgreementEntity> agreements = new ArrayList<>();
}
