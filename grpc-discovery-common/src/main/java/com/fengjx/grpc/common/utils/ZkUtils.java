
package com.fengjx.grpc.common.utils;

import cn.hutool.core.util.StrUtil;
import com.fengjx.grpc.common.config.ZkProperties;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author fengjianxin
 */
public class ZkUtils {

    private ZkUtils() {}

    public static CuratorFramework createCuratorFramework(ZkProperties zkProperties) throws InterruptedException {
        CuratorFramework client;
        RetryPolicy retryPolicy =
                new ExponentialBackoffRetry(zkProperties.getBaseSleepTimeMs(), zkProperties.getMaxRetries());
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(zkProperties.getConnectString())
                .sessionTimeoutMs(zkProperties.getSessionTimeoutMs())
                .connectionTimeoutMs(zkProperties.getConnectionTimeoutMs())
                .retryPolicy(retryPolicy);
        if (StrUtil.isNotBlank(zkProperties.getNamespace())) {
            builder.namespace(zkProperties.getNamespace());
        }
        client = builder.build();
        client.start();
        client.blockUntilConnected();
        return client;
    }


}
