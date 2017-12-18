package io.coinshell.web;

import io.coinshell.cli.CoinShell;
import io.coinshell.exception.ServiceException;
import io.coinshell.license.LicenseNotice;
import io.coinshell.web.response.VersionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Properties;

@RestController
public class VersionController extends BaseController {

    @Autowired
    public VersionController() {

    }


    @GetMapping("/version")
    @ResponseBody
    public VersionResponse getVersion() {


        Properties prop = new Properties();

        String resourceName = "application.properties";

        try {
            prop.load(VersionController.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
           throw new ServiceException(e);
        }

        String version = prop.getProperty("version");

        VersionResponse response = new VersionResponse();
        response.setVersion(version);
        response.setLicense(LicenseNotice.getLicense().trim());

        return response;

    }

}
