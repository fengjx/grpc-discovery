
package com.fengjx.grpc.common.utils;

import cn.hutool.core.util.StrUtil;
import com.fengjx.grpc.common.config.ZkConfiguration;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author fengjianxin
 */
public class ZkUtils {

    private ZkUtils() {}

    public static CuratorFramework createCuratorFramework(ZkConfiguration zkConfiguration) throws InterruptedException {
        return createCuratorFramework(zkConfiguration.getConnectString(), zkConfiguration.getNamespace(),
                zkConfiguration.getSessionTimeoutMs(), zkConfiguration.getConnectionTimeoutMs(),
                zkConfiguration.getBaseSleepTimeMs(), zkConfiguration.getMaxRetries());
    }

    public static CuratorFramework createCuratorFramework(String connectString, String namespace, int sessionTimeoutMs,
            int connectionTimeoutMs, int baseSleepTimeMs, int maxRetries) throws InterruptedException {
        CuratorFramework client;
        RetryPolicy retryPolicy =
                new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .sessionTimeoutMs(sessionTimeoutMs)
                .connectionTimeoutMs(connectionTimeoutMs)
                .retryPolicy(retryPolicy);
        if (StrUtil.isNotBlank(namespace)) {
            builder.namespace(namespace);
        }
        client = builder.build();
        client.start();
        client.blockUntilConnected();
        return client;
    }


}
