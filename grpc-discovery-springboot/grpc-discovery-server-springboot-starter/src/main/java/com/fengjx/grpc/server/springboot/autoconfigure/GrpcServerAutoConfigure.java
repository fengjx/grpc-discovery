
package com.fengjx.grpc.server.springboot.autoconfigure;

import cn.hutool.core.util.StrUtil;
import com.fengjx.grpc.common.discovery.DefaultServiceInstance;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fengjx.grpc.common.discovery.ServiceInstance;
import com.fengjx.grpc.common.springboot.autoconfigure.CuratorAutoConfigure;
import com.fengjx.grpc.server.registry.ServerRegistration;
import com.fengjx.grpc.server.registry.ZkServerRegistration;
import com.fengjx.grpc.server.server.GrpcServer;
import com.fengjx.grpc.server.server.ZkGrpcServer;
import com.fengjx.grpc.server.springboot.config.GrpcServerProperties;


/**
 * @author fengjianxin
 */
@Configuration
@AutoConfigureAfter(CuratorAutoConfigure.class)
@EnableConfigurationProperties(GrpcServerProperties.class)
public class GrpcServerAutoConfigure {

    @Value("${spring.application.name}")
    private String appName;


    public ServiceInstance serviceInstance(final GrpcServerProperties properties) {
        DefaultServiceInstance instance = new DefaultServiceInstance();
        if (StrUtil.isNotBlank(properties.getServiceId())) {
            appName = properties.getServiceId();
        }



        return instance;
    }


    @Bean
    @ConditionalOnMissingBean
    public ServerRegistration serverRegistration(final CuratorFramework client) {
        return new ZkServerRegistration(client);
    }

    public GrpcServer getGrpcServer(final ServerRegistration serverRegistration) {
        return new ZkGrpcServer(instance).registration(serverRegistration);
    }


}
