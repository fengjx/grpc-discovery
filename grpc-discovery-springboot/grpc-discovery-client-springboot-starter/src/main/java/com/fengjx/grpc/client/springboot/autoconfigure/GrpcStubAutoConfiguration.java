
package com.fengjx.grpc.client.springboot.autoconfigure;

import com.fengjx.grpc.client.GrpcClient;
import com.fengjx.grpc.client.springboot.inject.GrpcStubBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fengjianxin
 */
@Configuration
@ConditionalOnBean(GrpcClient.class)
public class GrpcStubAutoConfiguration {

    @Bean
    public static GrpcStubBeanPostProcessor grpcStubBeanPostProcessor(final ApplicationContext applicationContext,
            final GrpcClient grpcClient) {
        return new GrpcStubBeanPostProcessor(applicationContext, grpcClient);
    }

}
