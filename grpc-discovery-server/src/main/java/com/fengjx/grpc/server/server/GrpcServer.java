package com.fengjx.grpc.server.server;

import com.fengjx.grpc.common.discovery.ServiceInstance;
import com.fengjx.grpc.server.registry.ServerRegistration;
import com.google.common.collect.Lists;
import com.google.common.net.InetAddresses;
import io.grpc.*;
import io.grpc.netty.NettyServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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

    private GrpcServer() {
    }

    public static GrpcServer newInstance(ServiceInstance serviceInstance) {
        GrpcServer grpcServer = new GrpcServer();
        grpcServer.serviceInstance = serviceInstance;
        return grpcServer;
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
        NettyServerBuilder serverBuilder = newServerBuilder();
        configureServices(serverBuilder);
        configureBindableServices(serverBuilder);
        server = serverBuilder.build();
        server.start();
        registration.registry(serviceInstance);
        log.info("gRPC Server started, listening on address: {}, port: {}", serviceInstance.getHost(),
                serviceInstance.getPort());
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
        if (StringUtils.isBlank(host)) {
            return NettyServerBuilder.forPort(port);
        } else {
            return NettyServerBuilder.forAddress(new InetSocketAddress(InetAddresses.forString(host), port));
        }
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
}
