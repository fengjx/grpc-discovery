package com.fengjx.grpc.server.registry;

import com.fengjx.grpc.common.config.ZkProperties;
import com.fengjx.grpc.common.discovery.GsonInstanceSerializer;
import com.fengjx.grpc.common.discovery.InstanceSerializer;
import com.fengjx.grpc.common.discovery.ServiceInstance;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author fengjianxin
 */
@Slf4j
public class ZkServerRegistration implements ServerRegistration {

    private InstanceSerializer serializer = new GsonInstanceSerializer();
    private CuratorFramework client;
    private ZkProperties zkProperties;

    public ZkServerRegistration(ZkProperties zkProperties) throws InterruptedException {
        this.zkProperties = zkProperties;
        init();
    }

    @Override
    public void registry(ServiceInstance serviceInstance) throws Exception {
        serviceInstance.getMetadata().put(ServiceInstance.METADATA_KEY_START_TIME, System.currentTimeMillis());
        byte[] bytes = serializer.serialize(serviceInstance);
        String servicePath = "/" + serviceInstance.getServiceId();
        String path = servicePath + "/" + serviceInstance.getIp() + ":" + serviceInstance.getPort();
        client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, bytes);
    }

    private void init() throws InterruptedException {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory.builder().connectString(zkProperties.getConnectString())
                .sessionTimeoutMs(3000).connectionTimeoutMs(1000).retryPolicy(retryPolicy)
                .namespace(zkProperties.getNamespace()).build();
        this.client.start();
        this.client.blockUntilConnected();
        log.info("zk server registration init.");
    }
}
