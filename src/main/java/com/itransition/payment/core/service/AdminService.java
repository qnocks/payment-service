package com.itransition.payment.core.service;

import com.itransition.payment.core.dto.TransactionAdminDto;
import java.util.List;

public interface AdminService {

    List<TransactionAdminDto> searchTransactions();

    TransactionAdminDto updateTransaction(TransactionAdminDto transactionAdminDto);

    TransactionAdminDto completeTransaction(String externalId, String provider);
}
