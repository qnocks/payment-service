package com.itransition.payment.administration.service;

import com.itransition.payment.core.dto.TransactionStateDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    List<TransactionStateDto> searchTransactions(Pageable pageable);

    TransactionStateDto updateTransaction(TransactionStateDto adminDto);

    TransactionStateDto completeTransaction(String externalId, String provider);
}
