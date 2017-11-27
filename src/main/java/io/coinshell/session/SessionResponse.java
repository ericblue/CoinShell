package io.coinshell.session;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SessionResponse {

    private String jwt;

}
