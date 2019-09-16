
package com.fengjx.grpc.server.springboot.autoconfigure;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fengjx.grpc.common.discovery.DefaultServiceInstance;
import com.fengjx.grpc.common.discovery.ServiceInstance;
import com.fengjx.grpc.common.springboot.autoconfigure.CuratorAutoConfigure;
import com.fengjx.grpc.server.registry.ServerRegistration;
import com.fengjx.grpc.server.registry.ZkServerRegistration;
import com.fengjx.grpc.server.server.GrpcServer;
import com.fengjx.grpc.server.server.ZkGrpcServer;
import com.fengjx.grpc.server.springboot.config.GrpcServerProperties;
import com.fengjx.grpc.server.springboot.server.GrpcServerLifecycle;

import cn.hutool.core.util.StrUtil;


/**
 * @author fengjianxin
 */
@Configuration
@AutoConfigureAfter(CuratorAutoConfigure.class)
@EnableConfigurationProperties(GrpcServerProperties.class)
public class GrpcServerAutoConfigure {

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    @ConditionalOnMissingBean
    public ServerRegistration serverRegistration(final CuratorFramework client) {
        return new ZkServerRegistration(client);
    }

    @Bean
    @ConditionalOnMissingBean
    public ServiceInstance serviceInstance(final GrpcServerProperties properties) {
        DefaultServiceInstance instance = new DefaultServiceInstance();
        if (StrUtil.isNotBlank(properties.getServiceId())) {
            appName = properties.getServiceId();
        }
        instance.setServiceId(appName);
        instance.setHost(properties.getHost());
        instance.setPort(properties.getPort());
        if (StrUtil.isNotBlank(properties.getIp())) {
            instance.setIp(properties.getIp());
        }
        return instance;
    }

    @Bean
    @ConditionalOnBean({ServiceInstance.class, ServerRegistration.class})
    @ConditionalOnMissingBean
    public GrpcServer grpcServer(final ServiceInstance serviceInstance, final ServerRegistration serverRegistration) {
        return new ZkGrpcServer(serviceInstance).registration(serverRegistration);
    }

    @Bean
    @ConditionalOnBean(GrpcServer.class)
    @ConditionalOnMissingBean
    public GrpcServerLifecycle grpcServerLifecycle(final ApplicationContext applicationContext, GrpcServer grpcServer) {
        return new GrpcServerLifecycle(applicationContext, grpcServer);
    }


}
