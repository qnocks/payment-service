package com.itransition.payment.transaction.entity;

import com.itransition.payment.transaction.entity.Transaction;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Entity
@Table(name = "payment_providers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "transactions")
public class PaymentProvider {

    @Id
    private String name;

    @Generated(GenerationTime.INSERT)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "provider")
    private List<Transaction> transactions;
}
