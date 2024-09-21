package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.CurrencyDto;
import com.example.ecommerce.entity.CurrencyEntity;
import com.example.ecommerce.repository.CurrencyRepository;
import com.example.ecommerce.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@RequiredArgsConstructor

public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final RestTemplate restTemplate;

//    @Value("${spring.application.apiUrl}")
//    private String apiUrl;

    private final Environment environment;


    @Override
    public void fetchCurrency() {
        ResponseEntity<CurrencyDto[]> response = restTemplate.getForEntity(Objects.requireNonNull(environment.getProperty("spring.application.apiUrl")), CurrencyDto[].class);
        if (response.getStatusCode() == HttpStatus.OK) {
            CurrencyDto[] currency = response.getBody();
            if (!ObjectUtils.isEmpty(currency)) {
                for (CurrencyDto c : currency) {
                    currencyRepository.save(CurrencyEntity
                            .builder()
                            .title(c.getTitle())
                            .code(c.getCode())
                            .cbPrice(c.getCb_price())
                            .nbuSellPrice(c.getNbu_cell_price())
                            .nbuBuyPrice(c.getNbu_buy_price())
                            .date(c.getDate())
                            .build()
                    );
                }
            }
        }
    }
}
