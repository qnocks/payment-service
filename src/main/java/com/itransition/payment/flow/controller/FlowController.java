package com.itransition.payment.flow.controller;

import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.flow.service.FlowService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("classpath:/messages/swagger/swagger.properties")
public class FlowController {

    private final FlowService flowService;

    @ApiOperation(value = "${swagger.flow.create-transaction}", response = TransactionInfoDto.class)
    @PostMapping
    public TransactionInfoDto createTransaction(
            @RequestBody TransactionStateDto stateDto) {
        return flowService.createTransaction(stateDto);
    }

    @ApiOperation(value = "${swagger.flow.update-transaction}", response = TransactionInfoDto.class)
    @PutMapping
    public TransactionInfoDto updateTransaction(@RequestBody TransactionInfoDto updateDto) {
        return flowService.updateTransaction(updateDto);
    }

    @ApiOperation(value = "${swagger.flow.search-transactions}", response = TransactionInfoDto.class)
    @GetMapping(params = {"external_id", "provider"})
    public List<TransactionInfoDto> searchTransaction(
            @RequestParam("external_id") String externalId,
            @RequestParam("provider") String provider) {
        return flowService.searchTransactions(externalId, provider);
    }
}
