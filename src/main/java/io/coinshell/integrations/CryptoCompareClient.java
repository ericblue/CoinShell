package io.coinshell.integrations;

import com.jayway.jsonpath.JsonPath;
import io.coinshell.domain.Price;
import io.coinshell.exception.RestClientException;
import io.coinshell.exception.ServiceException;
import io.coinshell.rest.RestClient;
import io.coinshell.rest.RestClientOptions;
import io.coinshell.rest.RestOperation;
import io.coinshell.service.CoinService;
import org.apache.commons.collections4.MapUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class CryptoCompareClient {

    private final Logger logger = LoggerFactory.getLogger(CryptoCompareClient.class);


    private String PRICE_URL = "https://min-api.cryptocompare.com/data/price";

    private String HISTORICAL_PRICE_URL = "https://min-api.cryptocompare.com/data/pricehistorical";


    private RestClient restClient;


    public CryptoCompareClient() {

        RestClientOptions restClientOptions = new RestClientOptions();

        restClientOptions.setUseProxy(false);

        restClient = new RestClient(restClientOptions);

    }

    public Price getPrice(String fromCurrencyCode, String toCurrencyCode) {


        Price price = new Price();


        String json;

        // Ex: ?fsym=BTC&tsyms=USD"
        String url = PRICE_URL + "?fsym=" + fromCurrencyCode + "&tsyms=" + toCurrencyCode;

        try {
            json = (String) restClient.makeRequest(RestOperation.GET, url, null, new String());
        } catch (RestClientException e) {
            logger.debug("Caught " + e.getMessage());
            throw new RuntimeException(e);
        }


        HashMap<String,Object> priceData = JsonPath.read(json, "$");

        logger.debug("priceData = " + priceData);


        Double amount = MapUtils.getDouble(priceData, toCurrencyCode);
        if (amount == null) {
            throw new ServiceException("Can't find price for currency pair [" + fromCurrencyCode + ":" + toCurrencyCode + "]");
        }

        price.setCurrencyCode(toCurrencyCode);
        price.setPrice(amount);

        return price;

    }

    public Price getPriceHistorical(DateTime timestamp, String fromCurrencyCode, String toCurrencyCode) {


        Long time = timestamp.toDate().getTime() / 1000;

        Price price = new Price();

        String json;

        // Ex: ?fsym=BTC&tsyms=USD&ts=1510630623"
        String url = HISTORICAL_PRICE_URL + "?fsym=" + fromCurrencyCode + "&tsyms=" + toCurrencyCode + "&ts=" + time;
        logger.debug("Calling URL " + url);

        try {
            json = (String) restClient.makeRequest(RestOperation.GET, url, null, new String());
        } catch (RestClientException e) {
            logger.debug("Caught " + e.getMessage());
            throw new RuntimeException(e);
        }


        HashMap<String,Object> priceData = JsonPath.read(json, "$");

        logger.debug("priceData = " + priceData);

        HashMap<String,Object> priceForCurrency = (HashMap<String, Object>) MapUtils.getMap(priceData, fromCurrencyCode);

        Double amount = MapUtils.getDouble(priceForCurrency, toCurrencyCode);
        if (amount == null) {
            throw new ServiceException("Can't find price for currency pair [" + fromCurrencyCode + ":" + toCurrencyCode + "]");
        }

        price.setCurrencyCode(toCurrencyCode);
        price.setPrice(amount);

        return price;

    }

    public static void main(String[] args) {

        CryptoCompareClient client = new CryptoCompareClient();

        Price price = client.getPrice("BTC", "USD");

    }



}
