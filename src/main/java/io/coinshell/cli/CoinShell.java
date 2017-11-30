package io.coinshell.cli;


import io.coinshell.CoinShellServer;
import io.coinshell.domain.Price;
import io.coinshell.integrations.CryptoCompareClient;
import io.coinshell.service.CoinService;
import io.coinshell.util.SpringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

@SpringBootApplication
public class CoinShell {

    private static final Logger logger = LoggerFactory.getLogger(CoinShell.class);

    private static Options options = new Options();

    private static CryptoCompareClient cryptoCompareClient = new CryptoCompareClient();

    public static void raiseError(String message) {

        System.out.println("Error: " + message);
        logger.error(message);
        System.exit(-1);


    }

    public static void main(String[] args) {


        Option helpOption = Option.builder("h")
                .longOpt("help")
                .required(false)
                .desc("shows this message")
                .build();


        Option webServerOption = Option.builder("w")
                .longOpt("web")
                .required(false)
                .desc("launch embedded web server")
                .build();

        Option priceOption = Option.builder("p")
                .longOpt("price")
                .required(false)
                .desc("Find current price of a coin")
                .build();


        options.addOption(helpOption);
        options.addOption(webServerOption);
        options.addOption(priceOption);


        try {

            CommandLineParser parser = new DefaultParser();
            CommandLine cmdLine = parser.parse(options, args);


            if (cmdLine.hasOption("help")) {
                printUsage();
            }
            else if (cmdLine.hasOption("web")) {
                System.out.println("Running...");
                SpringApplication.run(CoinShellServer.class);
            }
            else if (cmdLine.hasOption("price")) {

                List<String> argList = cmdLine.getArgList();

                if (argList.size() < 2) {
                    raiseError("usage - coinshell -p <fromCurrency> <toCurrency>");
                }

                String fromCurrency = argList.get(0);
                String toCurrency = argList.get(1);

                System.out.println("arg list = " + argList);

                Price price = cryptoCompareClient.getPrice(fromCurrency, toCurrency);
                System.out.println(fromCurrency + " price = " + price.getPrice() + " "  + toCurrency);

            }
            else {
                printUsage();
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.out.println("Try \"--help\" option for details.");

        }


    }




    public static void printUsage() {

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("coinshell", options);

    }


}
