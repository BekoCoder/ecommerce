package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.CurrencyDto;
import com.example.ecommerce.entity.CurrencyEntity;
import com.example.ecommerce.repository.CurrencyRepository;
import com.example.ecommerce.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
@RequiredArgsConstructor

public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final RestTemplate restTemplate;

    @Value("${spring.application.apiUrl}")
    private String apiUrl;


    @Override
    @Transactional
    public void fetchCurrency() {
        ResponseEntity<CurrencyDto[]> response = restTemplate.getForEntity(apiUrl, CurrencyDto[].class);
        if (response.getStatusCode() == HttpStatus.OK) {
            CurrencyDto[] currency = response.getBody();
            if (currency != null) {
                Arrays.stream(currency).forEach(c -> {
                    CurrencyEntity currencyEntity = new CurrencyEntity();
                    currencyEntity.setTitle(c.getTitle());
                    currencyEntity.setCode(c.getCode());
                    currencyEntity.setCbPrice(c.getCb_price());
                    currencyEntity.setNbuSellPrice(c.getNbu_cell_price());
                    currencyEntity.setNbuBuyPrice(c.getNbu_buy_price());
                    currencyEntity.setDate(c.getDate());
                    currencyRepository.save(currencyEntity);
                });
            }

        }

    }

}
