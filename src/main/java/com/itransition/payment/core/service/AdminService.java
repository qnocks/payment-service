package com.itransition.payment.core.service;

import com.itransition.payment.core.dto.TransactionStateDto;
import java.util.List;

public interface AdminService {

    List<TransactionStateDto> searchTransactions(int page, int pageSize, String  sort, String order, String value);

    TransactionStateDto updateTransaction(TransactionStateDto adminDto);

    TransactionStateDto completeTransaction(String externalId, String provider);
}
