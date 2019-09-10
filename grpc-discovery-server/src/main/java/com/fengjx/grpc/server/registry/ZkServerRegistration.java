package com.fengjx.grpc.server.registry;

import com.fengjx.grpc.common.config.ZkProperties;
import com.fengjx.grpc.common.discovery.ServiceInstance;
import com.fengjx.grpc.common.utils.JsonUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author fengjianxin
 */
public class ZkServerRegistration implements ServerRegistration {

    private CuratorFramework client;
    private ZkProperties zkProperties;

    public ZkServerRegistration(ZkProperties zkProperties) throws InterruptedException {
        this.zkProperties = zkProperties;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory.builder().connectString(zkProperties.getConnectString())
                .sessionTimeoutMs(3000).connectionTimeoutMs(1000).retryPolicy(retryPolicy)
                .namespace(zkProperties.getNamespace()).build();
        this.client.start();
        this.client.blockUntilConnected();
    }

    @Override
    public void registry(ServiceInstance serviceInstance) throws Exception {
        serviceInstance.getMetadata().put(ServiceInstance.METADATA_KEY_START_TIME, System.currentTimeMillis());
        byte[] bytes = JsonUtils.toJson(serviceInstance).getBytes();
        String path = "/" + serviceInstance.getHost() + ":" + serviceInstance.getPort();
        client.create().withMode(CreateMode.EPHEMERAL).forPath(path, bytes);
    }
}
