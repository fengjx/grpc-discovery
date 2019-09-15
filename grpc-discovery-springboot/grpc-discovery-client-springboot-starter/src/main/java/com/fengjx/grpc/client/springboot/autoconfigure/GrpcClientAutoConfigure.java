
package com.fengjx.grpc.client.springboot.autoconfigure;

import com.fengjx.grpc.client.GrpcClient;
import com.fengjx.grpc.client.ZkGrpcClient;
import com.fengjx.grpc.client.channel.GrpcChannelFactory;
import com.fengjx.grpc.client.channel.ShadedNettyChannelFactory;
import com.fengjx.grpc.client.resolver.ZkNameResolverProvider;
import com.fengjx.grpc.common.springboot.autoconfigure.CuratorAutoConfigure;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author fengjianxin
 */
@Configuration
@AutoConfigureAfter(CuratorAutoConfigure.class)
public class GrpcClientAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    public ZkNameResolverProvider zkNameResolverProvider(final CuratorFramework client) {
        return new ZkNameResolverProvider(client);
    }

    @Bean
    @ConditionalOnMissingBean
    public GrpcChannelFactory grpcChannelFactory(ZkNameResolverProvider zkNameResolverProvider) {
        return new ShadedNettyChannelFactory(zkNameResolverProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public GrpcClient grpcClient(GrpcChannelFactory grpcChannelFactory) {
        return new ZkGrpcClient(grpcChannelFactory);
    }


}
