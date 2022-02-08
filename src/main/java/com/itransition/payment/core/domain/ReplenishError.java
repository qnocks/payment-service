package com.itransition.payment.core.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "replenish_errors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplenishError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long timestamp;
    private String error;

    @ManyToOne(fetch = FetchType.EAGER)
    private Transaction transaction;
}
