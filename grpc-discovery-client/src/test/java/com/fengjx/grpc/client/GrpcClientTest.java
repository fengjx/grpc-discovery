package com.fengjx.grpc.client;

import com.fengjx.grpc.client.config.ZkConfig;
import com.fengjx.grpc.common.config.ZkConfiguration;
import org.junit.Before;

/**
 * @author fengjianxin
 */
public class GrpcClientTest {

    private GrpcClient grpcClient;

    @Before
    public void before() {
        ZkConfiguration zkConfiguration = new ZkConfiguration();
        zkConfiguration.setConnectString(ZkConfig.ZK_PROPERTIES.getConnectString());
        zkConfiguration.setNamespace(ZkConfig.ZK_PROPERTIES.getNamespace());
        grpcClient = new ZkGrpcClient(zkConfiguration);
    }


}
