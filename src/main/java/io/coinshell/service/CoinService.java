package io.coinshell.service;

import io.coinshell.domain.Price;
import io.coinshell.integrations.CryptoCompareClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoinService {

    private final Logger logger = LoggerFactory.getLogger(CoinService.class);

    @Autowired
    private CryptoCompareClient cryptoCompareClient;

    @Autowired
    public CoinService() {

    }

    public Price getPrice(String fromCurrencyCode, String toCurrencyCode) {

        return cryptoCompareClient.getPrice(fromCurrencyCode, toCurrencyCode);

    }


}
