package com.itransition.payment.flow.controller;

import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.flow.service.FlowService;
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
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Tag(name = "Flow Controller", description = "API for management and lifecycle of transactions")
public class FlowController {

    private final FlowService flowService;

    @Operation(summary = "Update Transaction", description = "Updates transaction according to allowed dataflow")
    @PutMapping
    public TransactionInfoDto updateTransaction(@RequestBody TransactionInfoDto updateDto) {
        return flowService.updateTransaction(updateDto);
    }

    @Operation(
            summary = "Create Transaction",
            description = "Creates transaction with the provided amount in Initial state"
    )
    @PostMapping
    public TransactionInfoDto createTransaction(
            @RequestBody TransactionStateDto stateDto) {
        return flowService.createTransaction(stateDto);
    }

    @Operation(summary = "Search transactions", description = "Retrieves transactions with provided external id")
    @GetMapping(params = {"external_id", "provider"})
    public List<TransactionInfoDto> searchTransaction(
            @RequestParam("external_id") String externalId,
            @RequestParam("provider") String provider) {
        return flowService.searchTransactions(externalId, provider);
    }
}
