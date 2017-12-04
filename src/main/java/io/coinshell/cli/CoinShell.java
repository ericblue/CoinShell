package io.coinshell.cli;


import io.coinshell.CoinShellServer;
import io.coinshell.domain.Price;
import io.coinshell.integrations.CryptoCompareClient;
import io.coinshell.license.LicenseNotice;
import io.coinshell.service.CoinService;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

// TODO Get Spring autoconfig working with both CLI and Web concurrently
// See https://kanbanflow.com/t/ce5d707b257d11e0e1ba0962aee88d57/cs2
// For now, use rest clients directly rather than Service layers

//@SpringBootApplication
//@EnableAutoConfiguration
//@ComponentScan("io.coinshell")
public class CoinShell implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CoinShell.class);

    private static Options options = new Options();

    private static CryptoCompareClient cryptoCompareClient = new CryptoCompareClient();

    //private CoinService coinService;

//    @Autowired
//    public CoinShell(CoinService coinService) {
//
//        this.coinService = coinService;
//    }

    public static void raiseError(String message) {

        System.out.println("Error: " + message);
        System.exit(-1);

    }

    private static String getVersion() throws IOException {

        Properties prop = new Properties();

        String resourceName = "application.properties";

        prop.load(CoinShell.class.getResourceAsStream("/application.properties"));

        String version = prop.getProperty("version");
        if (version != null) {
            return version;
        } else {
            return "Undefined";
        }

    }

    public static void main(String[] args) {

        System.out.println("Welcome! CoinShell Initializing...");

        SpringApplication app = new SpringApplication(CoinShell.class);
        app.setWebEnvironment(false);
        app.run(args);

    }

    @Override
    public void run(String... args) throws Exception {


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

        Option versionOption = Option.builder("v")
                .longOpt("version")
                .required(false)
                .desc("Get version number")
                .build();


        options.addOption(helpOption);
        options.addOption(webServerOption);
        options.addOption(priceOption);
        options.addOption(versionOption);


        try {

            CommandLineParser parser = new DefaultParser();
            CommandLine cmdLine = parser.parse(options, args);

            Boolean validCommand = false;


            if (cmdLine.hasOption("web")) {
                validCommand = true;
                System.out.println("Launching embedded CoinShell web server...");
                SpringApplication.run(CoinShellServer.class);
            } else {

                System.out.println("Launching CLI...");

                if (cmdLine.hasOption("help")) {
                    validCommand = true;
                    printUsage();
                    System.exit(0);
                }

                if (cmdLine.hasOption("version")) {
                    validCommand = true;

                    System.out.println("CoinShell version " + getVersion() + "\n");
                    System.out.println(LicenseNotice.getLicense());
                    System.exit(0);
                }

                if (cmdLine.hasOption("price")) {

                    validCommand = true;

                    System.out.println("Making request...");

                    List<String> argList = cmdLine.getArgList();

                    if (argList.size() < 2) {
                        raiseError("usage - coinshell -p <fromCurrency> <toCurrency>");
                    }

                    String fromCurrency = argList.get(0).toUpperCase();
                    String toCurrency = argList.get(1).toUpperCase();

                    logger.debug("arg list = " + argList);

                    Price price = cryptoCompareClient.getPrice(fromCurrency, toCurrency);
                    //Price price = coinService.getPrice(fromCurrency, toCurrency);
                    System.out.println(fromCurrency + " price = " + price.getPrice() + " " + toCurrency);
                    System.exit(0);

                }

                if (!validCommand) {

                    printUsage();
                }

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
