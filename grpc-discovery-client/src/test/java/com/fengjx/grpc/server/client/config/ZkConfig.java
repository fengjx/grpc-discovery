
package com.fengjx.grpc.server.client.config;

import com.fengjx.grpc.common.config.ZkProperties;

/**
 * @author fengjianxin
 */
public class ZkConfig {


    public static final ZkProperties ZK_PROPERTIES = new ZkProperties();

    static {
        ZK_PROPERTIES.setConnectString("localhost:2181");
        ZK_PROPERTIES.setNamespace("grpc-example");
    }

}
