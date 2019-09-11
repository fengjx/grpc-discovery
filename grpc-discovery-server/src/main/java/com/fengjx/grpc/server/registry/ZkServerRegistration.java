
package com.fengjx.grpc.server.registry;

import com.fengjx.grpc.common.config.ZkProperties;
import com.fengjx.grpc.common.discovery.GsonInstanceSerializer;
import com.fengjx.grpc.common.discovery.InstanceSerializer;
import com.fengjx.grpc.common.discovery.ServiceInstance;
import com.fengjx.grpc.common.utils.ZkUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

/**
 * @author fengjianxin
 */
@Slf4j
public class ZkServerRegistration implements ServerRegistration {

    private InstanceSerializer serializer = new GsonInstanceSerializer();
    private CuratorFramework client;

    public ZkServerRegistration(ZkProperties zkProperties) throws InterruptedException {
        this(ZkUtils.createCuratorFramework(zkProperties));
    }

    public ZkServerRegistration(CuratorFramework client) {
        this.client = client;
        log.info("zk server registration init.");
    }


    @Override
    public void registry(ServiceInstance serviceInstance) throws Exception {
        serviceInstance.getMetadata().put(ServiceInstance.METADATA_KEY_START_TIME, System.currentTimeMillis());
        byte[] bytes = serializer.serialize(serviceInstance);
        String servicePath = "/" + serviceInstance.getServiceId();
        String path = servicePath + "/" + serviceInstance.getIp() + ":" + serviceInstance.getPort();
        client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, bytes);
        log.info("service registry, path: {}, serviceInstance: {}", path, new String(bytes));
    }

}
