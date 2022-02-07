package com.itransition.payment.core.domain;

import com.itransition.payment.core.domain.enums.ReplenishmentStatus;
import com.itransition.payment.core.domain.enums.TransactionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "transaction")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "provider")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "core_id")
    private String coreId;

    // TODO: Delete either provider or paymentProvider according to specification
//    private String provider;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    private PaymentProvider provider;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    private ReplenishmentStatus replenishmentStatus;

    private BigDecimal amount;
    private String currency;

    @Column(name = "commission_amount")
    private BigDecimal commissionAmount;

    @Column(name = "commission_currency")
    private String commissionCurrency;

    @Column(name = "user_id")
    private String userId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "transaction")
    private List<ReplenishError> replenishErrors;

    @Column(name = "external_date")
    private LocalDateTime externalDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "replenish_after")
    private LocalDateTime replenishAfter;

    @Column(name = "additional_data")
    private String additionalData;
}
