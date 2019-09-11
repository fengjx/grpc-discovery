
package com.fengjx.grpc.server;

import com.fengjx.grpc.common.discovery.ServiceInstance;
import com.fengjx.grpc.server.registry.ServerRegistration;
import com.google.common.collect.Lists;
import com.google.common.net.InetAddresses;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerServiceDefinition;
import io.grpc.netty.NettyServerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author fengjianxin
 */
@Slf4j
public class GrpcServer {

    private ServiceInstance serviceInstance;
    private Server server;
    private ServerRegistration registration;
    private List<ServerServiceDefinition> services = Lists.newArrayList();
    private List<BindableService> bindableServices = Lists.newArrayList();

    private GrpcServer() {}

    private GrpcServer(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public static GrpcServer newInstance(ServiceInstance serviceInstance) {
        return new GrpcServer(serviceInstance);
    }

    public GrpcServer addService(ServerServiceDefinition service) {
        services.add(service);
        return this;
    }

    public GrpcServer addService(BindableService bindableService) {
        bindableServices.add(bindableService);
        return this;
    }

    public GrpcServer registration(ServerRegistration registration) {
        this.registration = registration;
        return this;
    }

    public void start() throws Exception {
        start(false);
    }

    public void start(boolean block) throws Exception {
        NettyServerBuilder serverBuilder = newServerBuilder();
        configureServices(serverBuilder);
        configureBindableServices(serverBuilder);
        server = serverBuilder.build();
        server.start();
        log.info("gRPC Server started, binding on host: {}, port: {}", serviceInstance.getHost(),
                serviceInstance.getPort());
        registration.registry(serviceInstance);
        if (block) {
            blockUntilShutdown();
        }
    }

    public void destroy() {
        Server localServer = this.server;
        if (localServer != null) {
            localServer.shutdown();
            this.server = null;
            log.info("gRPC server shutdown");
        }
    }

    private NettyServerBuilder newServerBuilder() {
        final String host = serviceInstance.getHost();
        final int port = serviceInstance.getPort();
        return NettyServerBuilder.forAddress(new InetSocketAddress(InetAddresses.forString(host), port));
    }

    private void configureServices(ServerBuilder serverBuilder) {
        for (ServerServiceDefinition service : services) {
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
