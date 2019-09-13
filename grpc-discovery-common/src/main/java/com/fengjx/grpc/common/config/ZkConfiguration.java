
package com.fengjx.grpc.common.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author fengjianxin
 */
@Getter
@Setter
public class ZkConfiguration {

    private String connectString;
    private String namespace;

    private int connectionTimeoutMs = 500;
    private int sessionTimeoutMs = 1000;
    private int maxRetries = 5;
    private int baseSleepTimeMs = 500;

}
