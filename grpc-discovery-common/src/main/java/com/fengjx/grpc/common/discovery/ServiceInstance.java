package com.fengjx.grpc.common.discovery;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author fengjianxin
 */
@Getter
@Setter
@Builder
public class ServiceInstance {

    public static final String METADATA_KEY_START_TIME = "startTime";

    private String getServiceId;
    private String host;
    private int port;

    private Map<String, Object> metadata;

}
