package ru.gav19770210.stage2task05.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tpp_product_register", schema = "public", catalog = "stage2task5")
@Getter
@Setter
@NoArgsConstructor
public class TppProductRegisterEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TppProductEntity productId;

    @Column(name = "register_type")
    private String registerType;

    @JoinColumn(name = "account", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AccountEntity account;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "state")
    private String state;

    @Column(name = "account_number")
    private String accountNumber;

    public TppProductRegisterEntity(TppProductEntity productId, String registerType, AccountEntity account,
                                    String currencyCode, AccountState state, String accountNumber) {
        this.productId = productId;
        this.registerType = registerType;
        this.account = account;
        this.currencyCode = currencyCode;
        this.state = state.name();
        this.accountNumber = accountNumber;
    }
}
