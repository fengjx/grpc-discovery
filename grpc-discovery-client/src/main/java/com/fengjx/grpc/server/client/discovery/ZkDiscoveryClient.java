
package com.fengjx.grpc.server.client.discovery;

import com.fengjx.grpc.common.config.ZkProperties;
import com.fengjx.grpc.common.discovery.ServiceInstance;
import com.fengjx.grpc.common.utils.ZkUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.util.List;

/**
 * @author fengjianxin
 */
@Slf4j
public class ZkDiscoveryClient implements DiscoveryClient {

    public static final String DESCRIPTION = "gRPC zk discovery client";

    private CuratorFramework client;

    public ZkDiscoveryClient(ZkProperties zkProperties) throws InterruptedException {
        this(ZkUtils.createCuratorFramework(zkProperties));
    }

    public ZkDiscoveryClient(CuratorFramework client) {
        this.client = client;
        log.info("zk server registration init.");
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public List<ServiceInstance> getServices(String serviceId) {
        return null;
    }

    @Override
    public List<String> getServiceIds() {
        return null;
    }
}
