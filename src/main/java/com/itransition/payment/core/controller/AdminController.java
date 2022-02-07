package com.itransition.payment.core.controller;

import com.itransition.payment.core.dto.TransactionAdminDto;
import com.itransition.payment.core.service.AdminService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/transactions")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping(params = {"page", "sort", "order"})
    public List<TransactionAdminDto> searchTransactions() {
        return adminService.searchTransactions();
    }

    @PutMapping
    public TransactionAdminDto updateTransaction(@RequestBody TransactionAdminDto transactionAdminDto) {
        return adminService.updateTransaction(transactionAdminDto);
    }

    @PostMapping(params = {"external_id", "provider"})
    public TransactionAdminDto completeTransaction(
            @RequestParam("external_id") String externalId,
            @RequestParam("provider") String provider) {
        return adminService.completeTransaction(externalId, provider);
    }
}
