
package com.fengjx.grpc.server.springboot.server;

import com.fengjx.grpc.server.server.GrpcServer;
import com.fengjx.grpc.server.springboot.annotation.GrpcService;
import com.google.common.collect.Lists;
import io.grpc.BindableService;
import io.grpc.ServerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author fengjianxin
 */
@Slf4j
public class GrpcServerLifecycle implements SmartLifecycle {

    private final ApplicationContext applicationContext;
    private final GrpcServer grpcServer;

    public GrpcServerLifecycle(final ApplicationContext applicationContext, final GrpcServer grpcServer) {
        this.applicationContext = applicationContext;
        this.grpcServer = grpcServer;
    }

    @Override
    public void start() {
        try {
            bindServices();
            grpcServer.start(true);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to start the gRPC server", e);
        }
    }

    @Override
    public void stop() {
        grpcServer.destroy();
    }

    @Override
    public boolean isRunning() {
        return this.grpcServer.isRunning();
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }


    private void bindServices() {
        String[] beanNames = this.applicationContext.getBeanNamesForAnnotation(GrpcService.class);
        for (String beanName : beanNames) {
            GrpcService annotation = applicationContext.findAnnotationOnBean(beanName, GrpcService.class);
            List<ServerInterceptor> interceptors = interceptorsFromAnnotation(annotation);
            BindableService bindableService = this.applicationContext.getBean(beanName, BindableService.class);
            if (CollectionUtils.isEmpty(interceptors)) {
                grpcServer.addService(bindableService);
            } else {
                grpcServer.addService(bindableService, interceptors);
            }
            log.debug("Found gRPC service, bean: {}, class: {}", beanName, bindableService.getClass().getName());
        }
    }

    private List<ServerInterceptor> interceptorsFromAnnotation(final GrpcService annotation) throws BeansException {
        if (annotation == null) {
            return null;
        }
        final List<ServerInterceptor> list = Lists.newArrayList();
        for (final Class<? extends ServerInterceptor> interceptorClass : annotation.interceptors()) {
            final ServerInterceptor serverInterceptor;
            if (this.applicationContext.getBeanNamesForType(ServerInterceptor.class).length > 0) {
                serverInterceptor = this.applicationContext.getBean(interceptorClass);
            } else {
                try {
                    serverInterceptor = interceptorClass.getConstructor().newInstance();
                } catch (final Exception e) {
                    throw new BeanCreationException("Failed to create interceptor instance", e);
                }
            }
            list.add(serverInterceptor);
        }
        for (final String interceptorName : annotation.interceptorNames()) {
            list.add(this.applicationContext.getBean(interceptorName, ServerInterceptor.class));
        }
        return list;
    }

}
