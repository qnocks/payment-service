package com.itransition.payment.core.controller;

import com.itransition.payment.core.service.ReplenishService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestControllerShouldBeDeleted {

    private final ReplenishService replenishService;

    @PostMapping
    void replenish() {
        replenishService.replenish();
    }
}
