
package com.fengjx.grpc.common.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author fengjianxin
 */
@Getter
@Setter
public class ZkProperties {

    private String connectString;
    private String namespace;

    private int connectionTimeoutMs = 500;
    private int sessionTimeoutMs = 3000;
    private int maxRetries = 5;
    private int baseSleepTimeMs = 1000;

}
