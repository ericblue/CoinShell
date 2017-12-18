package io.coinshell.web;

import io.coinshell.domain.Currency;
import io.coinshell.domain.Price;
import io.coinshell.exception.ServiceException;
import io.coinshell.license.LicenseNotice;
import io.coinshell.service.CoinService;
import io.coinshell.util.DateTimeUtils;
import io.coinshell.web.response.PriceResponse;
import io.coinshell.web.response.VersionResponse;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

@RestController
public class PriceController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CoinService coinService;


    @Autowired
    public PriceController(CoinService coinService) {
        this.coinService = coinService;
    }


    @GetMapping("/price")
    @ResponseBody
    public PriceResponse getPrice(
            @RequestParam(value = "from", required = true) String from,
            @RequestParam(value = "to", required = false) String to
    ) {


        if (to == null) {
            to = Currency.USD.toString();
        }

        Price price = coinService.getPrice(from, to);

        PriceResponse response = new PriceResponse();
        response.setPrice(price);

        return response;

    }

    @GetMapping("/price/historical")
    @ResponseBody
    public PriceResponse getPriceHistorical(
            @RequestParam(value = "time", required = true) String time,
            @RequestParam(value = "from", required = true) String from,
            @RequestParam(value = "to", required = false) String to
    ) {


        if (to == null) {
            to = Currency.USD.toString();
        }

        String format = DateTimeUtils.detectDateTimeFormat(time);
        DateTime dateTime = DateTimeUtils.getDateTimeFromString(time, format);

        Price price = coinService.getPriceHistorical(dateTime,from, to);

        PriceResponse response = new PriceResponse();
        response.setPrice(price);

        return response;

    }


}
