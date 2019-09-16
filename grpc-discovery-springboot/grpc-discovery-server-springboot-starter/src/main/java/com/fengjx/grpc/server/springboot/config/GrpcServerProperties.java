
package com.fengjx.grpc.server.springboot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fengjianxin
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "grpc.server")
public class GrpcServerProperties {

    private String serviceId;
    private String host = "0.0.0.0";
    private String ip;
    private int port;
    private Map<String, Object> metadata = new HashMap<>();

}
