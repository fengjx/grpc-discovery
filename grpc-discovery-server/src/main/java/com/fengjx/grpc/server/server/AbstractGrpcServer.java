package com.fengjx.grpc.server.server;

import com.fengjx.grpc.common.discovery.ServiceInstance;
import com.google.common.collect.Lists;
import io.grpc.*;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author fengjianxin
 */
@Slf4j
public abstract class AbstractGrpcServer implements GrpcServer {

    protected ServiceInstance serviceInstance;
    protected Server server;
    private List<ServerServiceDefinition> serviceDefinitions = Lists.newArrayList();
    private List<BindableService> bindableServices = Lists.newArrayList();

    public AbstractGrpcServer(ServiceInstance serviceInstance){
        this.serviceInstance = serviceInstance;
    }

    @Override
    public GrpcServer addService(ServerServiceDefinition service) {
        serviceDefinitions.add(service);
        return this;
    }

    @Override
    public GrpcServer addService(BindableService bindableService) {
        bindableServices.add(bindableService);
        return this;
    }

    @Override
    public GrpcServer addService(BindableService bindableService, List<ServerInterceptor> interceptors) {
        serviceDefinitions.add(ServerInterceptors.intercept(bindableService, interceptors));
        return this;
    }

    protected abstract NettyServerBuilder newServerBuilder();

    protected abstract void registry(ServiceInstance serviceInstance) throws Exception;

    @Override
    public void start() throws Exception {
        start(false);
    }

    @Override
    public void start(boolean block) throws Exception {
        NettyServerBuilder serverBuilder = newServerBuilder();
        configureServiceDefinitions(serverBuilder);
        configureBindableServices(serverBuilder);
        server = serverBuilder.build();
        server.start();
        log.info("gRPC Server started, binding on host: {}, port: {}", serviceInstance.getHost(),
                serviceInstance.getPort());
        registry(serviceInstance);
        if (block) {
            blockUntilShutdown();
        }
    }

    @Override
    public void destroy() {
        Server localServer = this.server;
        if (localServer != null) {
            localServer.shutdown();
            this.server = null;
            log.info("gRPC server shutdown");
        }
    }

    private void configureServiceDefinitions(ServerBuilder serverBuilder) {
        for (ServerServiceDefinition service : serviceDefinitions) {
            serverBuilder.addService(service);
        }
    }

    private void configureBindableServices(ServerBuilder serverBuilder) {
        for (BindableService bindableService : bindableServices) {
            serverBuilder.addService(bindableService);
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

}
