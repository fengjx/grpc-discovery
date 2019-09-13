
package com.fengjx.grpc.client.config;

import com.fengjx.grpc.common.config.ZkConfiguration;

/**
 * @author fengjianxin
 */
public class ZkConfig {

    public static final ZkConfiguration ZK_PROPERTIES = new ZkConfiguration();

    static {
        ZK_PROPERTIES.setConnectString("localhost:2181");
        ZK_PROPERTIES.setNamespace("grpc-example");
    }

}
