package com.itransition.payment.core.controller;

import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.service.FlowService;
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
public class FlowController {

    private final FlowService flowService;

    @PostMapping
    public TransactionInfoDto createTransaction(
            @RequestBody TransactionStateDto stateDto) {
        return flowService.createTransaction(stateDto);
    }

    @PutMapping
    public TransactionInfoDto updateTransaction(@RequestBody TransactionInfoDto updateDto) {
        return flowService.updateTransaction(updateDto);
    }

    @GetMapping(params = {"external_id", "provider"})
    public List<TransactionInfoDto> searchTransaction(
            @RequestParam("external_id") String externalId,
            @RequestParam("provider") String provider) {
        return flowService.searchTransactions(externalId, provider);
    }
}
