
package com.fengjx.grpc.server.server;

import com.fengjx.grpc.common.discovery.ServiceInstance;
import com.fengjx.grpc.server.registry.ServerRegistration;
import com.google.common.net.InetAddresses;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author fengjianxin
 */
@Slf4j
public class ZkGrpcServer extends AbstractGrpcServer {

    private ServerRegistration registration;

    public ZkGrpcServer(ServiceInstance serviceInstance) {
        super(serviceInstance);
    }

    public ZkGrpcServer registration(ServerRegistration registration) {
        this.registration = registration;
        return this;
    }

    @Override
    protected NettyServerBuilder newServerBuilder() {
        final String host = serviceInstance.getHost();
        final int port = serviceInstance.getPort();
        return NettyServerBuilder.forAddress(new InetSocketAddress(InetAddresses.forString(host), port));
    }

    @Override
    protected void registry(ServiceInstance serviceInstance) throws Exception {
        registration.registry(serviceInstance);
    }

}
