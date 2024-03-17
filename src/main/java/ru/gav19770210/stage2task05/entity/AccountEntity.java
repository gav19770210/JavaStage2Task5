package ru.gav19770210.stage2task05.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "account", schema = "public", catalog = "stage2task5")
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class AccountEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_pool_id")
    private Long accountPoolId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "busy")
    private boolean busy;
}
