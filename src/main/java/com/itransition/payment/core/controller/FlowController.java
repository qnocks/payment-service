package com.itransition.payment.core.controller;

import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.service.FlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class FlowController {

    private final FlowService flowService;

    @PostMapping
    public ResponseEntity<TransactionInfoDto> createTransaction(
            @RequestBody TransactionAdapterStateDto transactionAdapterStateDto) {
        return new ResponseEntity<>(flowService.createTransaction(transactionAdapterStateDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<TransactionInfoDto> updateTransaction(@RequestBody TransactionInfoDto transactionInfoDto) {
        return new ResponseEntity<>(flowService.updateTransaction(transactionInfoDto), HttpStatus.OK);
    }

    @GetMapping(params = {"external_id", "provider"})
    public ResponseEntity<List<TransactionInfoDto>> searchTransaction(
            @RequestParam("external_id") String externalId,
            @RequestParam("provider") String provider) {

        return new ResponseEntity<>(flowService.searchTransaction(externalId, provider), HttpStatus.OK);
    }
}
