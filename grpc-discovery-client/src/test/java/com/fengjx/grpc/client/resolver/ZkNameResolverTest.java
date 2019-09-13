
package com.fengjx.grpc.client.resolver;

import com.fengjx.grpc.client.config.ZkConfig;
import com.fengjx.grpc.common.utils.ZkUtils;
import org.apache.curator.framework.CuratorFramework;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author fengjianxin
 */
public class ZkNameResolverTest {

    private static final CountDownLatch LATCH = new CountDownLatch(1);
    private CuratorFramework client;

    @Before
    public void before() {
        try {
            this.client = ZkUtils.createCuratorFramework(ZkConfig.ZK_PROPERTIES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWatch() throws InterruptedException {
        ZkNameResolver resolver = new ZkNameResolver("hello-world", client);
        resolver.testWatch();
        LATCH.await();
    }


}
