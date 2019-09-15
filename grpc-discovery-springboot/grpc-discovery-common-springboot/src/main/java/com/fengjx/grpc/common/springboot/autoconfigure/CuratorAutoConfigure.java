
package com.fengjx.grpc.common.springboot.autoconfigure;

import com.fengjx.grpc.common.utils.ZkUtils;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fengjx.grpc.common.springboot.config.DiscovertZkProperties;

/**
 * @author fengjianxin
 */
@Configuration
@EnableConfigurationProperties(DiscovertZkProperties.class)
public class CuratorAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    public CuratorFramework curatorFramework(DiscovertZkProperties zkProperties) throws InterruptedException {
        return ZkUtils.createCuratorFramework(zkProperties.getConnectString(), zkProperties.getNamespace(),
                zkProperties.getSessionTimeoutMs(), zkProperties.getConnectionTimeoutMs(),
                zkProperties.getBaseSleepTimeMs(), zkProperties.getMaxRetries());
    }


}
