
package com.fengjx.grpc.example.server.proto.config;

import com.fengjx.grpc.common.config.ZkConfiguration;

/**
 * @author fengjianxin
 */
public class ZkConfig {

    public static final ZkConfiguration EXAMPLE_CONFIG = new ZkConfiguration();

    static {
        EXAMPLE_CONFIG.setConnectString("localhost:2181");
        EXAMPLE_CONFIG.setNamespace("grpc-example");
    }

}
