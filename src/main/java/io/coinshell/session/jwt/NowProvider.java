package io.coinshell.session.jwt;


import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class NowProvider {
    public Date getNow() {
        return new Date();
    }
}