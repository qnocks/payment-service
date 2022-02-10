package com.itransition.payment.core.service;

import com.itransition.payment.core.dto.TransactionAdminDto;
import java.util.List;

public interface AdminService {

    List<TransactionAdminDto> searchTransactions(int page, int pageSize, String  sort, String order, String value);

    TransactionAdminDto updateTransaction(TransactionAdminDto transactionAdminDto);

    TransactionAdminDto completeTransaction(String externalId, String provider);
}
