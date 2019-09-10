package com.fengjx.grpc.common.discovery;

import java.net.URI;
import java.util.Map;

/**
 * @author fengjianxin
 */
public interface ServiceInstance {

    public static final String METADATA_KEY_START_TIME = "startTime";

    String getServiceId();

    String getHost();

    String getIp();

    int getPort();

    URI getUri();

    Map<String, Object> getMetadata();

    default String getScheme() {
        return null;
    }

}
