package com.example.ecommerce.config;

import com.example.ecommerce.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrencyScheduler {
    private final CurrencyService currencyService;

    @Scheduled(fixedRate = 86400000)
    public void fetchCurrencyRates() {
        currencyService.fetchCurrency();
    }
}
