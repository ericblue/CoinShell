package io.coinshell.cli;


import io.coinshell.CoinShellServer;
import org.apache.commons.cli.*;
import org.springframework.boot.SpringApplication;


public class CoinShell {

    private static Options options = new Options();



    public static void main(String[] args) {


        Option help = Option.builder("h")
                .longOpt("help")
                .required(false)
                .desc("shows this message")
                .build();


        Option webServer = Option.builder("w")
                .longOpt("web")
                .required(false)
                .desc("launch embedded web server")
                .build();


        options.addOption(help);
        options.addOption(webServer);

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
