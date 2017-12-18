package io.coinshell.web.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VersionResponse {

    String version;
    String license;

}
