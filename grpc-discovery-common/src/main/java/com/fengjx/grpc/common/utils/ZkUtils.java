
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
        CuratorFramework client;
        RetryPolicy retryPolicy =
                new ExponentialBackoffRetry(zkConfiguration.getBaseSleepTimeMs(), zkConfiguration.getMaxRetries());
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(zkConfiguration.getConnectString())
                .sessionTimeoutMs(zkConfiguration.getSessionTimeoutMs())
                .connectionTimeoutMs(zkConfiguration.getConnectionTimeoutMs())
                .retryPolicy(retryPolicy);
        if (StrUtil.isNotBlank(zkConfiguration.getNamespace())) {
            builder.namespace(zkConfiguration.getNamespace());
        }
        client = builder.build();
        client.start();
        client.blockUntilConnected();
        return client;
    }


}
