package io.coinshell.test;

import com.jayway.jsonpath.JsonPath;
import io.coinshell.exception.RestClientException;
import io.coinshell.rest.RestClient;
import io.coinshell.rest.RestClientOptions;
import io.coinshell.rest.RestOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;


public class CryptoCompareTest {

    private static final Logger logger = LoggerFactory.getLogger(CryptoCompareTest.class);



    public static void main(String[] args) {

        String PRICE_URL = "https://min-api.cryptocompare.com/data/price";

        RestClient restClient;


        RestClientOptions restClientOptions = new RestClientOptions();

        restClientOptions.setUseProxy(true);

        restClient = new RestClient(restClientOptions);

        String json;

        String url = PRICE_URL + "?fsym=BTC&tsyms=USD";

        try {
            json = (String) restClient.makeRequest(RestOperation.GET, url, null, new String());
        } catch (RestClientException e) {
            logger.debug("Caught " + e.getMessage());
            throw new RuntimeException(e);
        }


        HashMap<String,Object> priceData = JsonPath.read(json, "$");

        logger.debug("priceData = " + priceData);



    }


}
