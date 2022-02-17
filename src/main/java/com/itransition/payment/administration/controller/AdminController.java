package com.itransition.payment.administration.controller;

import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.administration.service.AdminService;
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
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/admin/transactions")
@RequiredArgsConstructor
@PropertySource("classpath:/messages/swagger/swagger.properties")
@ApiIgnore
public class AdminController {

    private final AdminService adminService;

    @ApiOperation(value = "${swagger.admin.search-transactions}", response = TransactionStateDto.class)
    @GetMapping
    public List<TransactionStateDto> searchTransactions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "3") int pageSize,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "order", defaultValue = "DESC") String order,
            @RequestParam(value = "value", defaultValue = "id") String value) {
        return adminService.searchTransactions(page, pageSize, sort, order, value);
    }

    @ApiOperation(value = "${swagger.admin.update-transaction}", response = TransactionStateDto.class)
    @PutMapping
    public TransactionStateDto updateTransaction(@RequestBody TransactionStateDto adminDto) {
        return adminService.updateTransaction(adminDto);
    }

    @ApiOperation(value = "${swagger.admin.complete-transaction}", response = TransactionStateDto.class)
    @PostMapping(params = {"external_id", "provider"})
    public TransactionStateDto completeTransaction(
            @RequestParam("external_id") String externalId,
            @RequestParam("provider") String provider) {
        return adminService.completeTransaction(externalId, provider);
    }
}
