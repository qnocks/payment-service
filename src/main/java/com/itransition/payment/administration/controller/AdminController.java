package com.itransition.payment.administration.controller;

import com.itransition.payment.administration.service.AdminService;
import com.itransition.payment.core.dto.TransactionStateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Admin Controller", description = "API for system administrators to view and manage transactions")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "Search transactions", description = "Search for transactions based on provided parameters")
    @GetMapping
    public List<TransactionStateDto> searchTransactions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "3") int pageSize,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "order", defaultValue = "DESC") String order,
            @RequestParam(value = "value", defaultValue = "id") String value) {
        return adminService.searchTransactions(page, pageSize, sort, order, value);
    }

    @Operation(summary = "Update transaction", description = "Updates not system transaction fields")
    @PutMapping
    public TransactionStateDto updateTransaction(@RequestBody TransactionStateDto adminDto) {
        return adminService.updateTransaction(adminDto);
    }

    @Operation(
            summary = "Complete transaction",
            description = "Mark transaction as completed and initiate replenishment process"
    )
    @PostMapping(params = {"external_id", "provider"})
    public TransactionStateDto completeTransaction(
            @RequestParam("external_id") String externalId,
            @RequestParam("provider") String provider) {
        return adminService.completeTransaction(externalId, provider);
    }
}
