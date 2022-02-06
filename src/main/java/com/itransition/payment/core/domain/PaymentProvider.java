package com.itransition.payment.core.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import liquibase.pro.packaged.L;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_provider")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String provider;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "provider")
    private List<Transaction> transactions;
}
