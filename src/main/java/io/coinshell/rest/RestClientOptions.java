package io.coinshell.rest;

import lombok.*;


@Data
public class RestClientOptions {

    private String username;

    private String password;

    private String userHash;

    private Boolean useProxy;

    private String proxyHost;

    private Integer proxyPort;

    private String deviceName;

    private String baseUrl;

    public RestClientOptions() {

        this.useProxy = false;
        this.proxyHost = new String("localhost");
        this.proxyPort = new Integer(8888);
        this.userHash = "-";

    }

}

