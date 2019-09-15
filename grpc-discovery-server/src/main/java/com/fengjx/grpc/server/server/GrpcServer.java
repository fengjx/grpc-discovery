package com.fengjx.grpc.server.server;

import io.grpc.BindableService;
import io.grpc.ServerInterceptor;
import io.grpc.ServerServiceDefinition;

import java.util.Collections;
import java.util.List;

/**
 * @author fengjianxin
 */
public interface GrpcServer {

    void start() throws Exception;

    void start(boolean block) throws Exception;

    void destroy() throws Exception;

    GrpcServer addService(ServerServiceDefinition service);

    default GrpcServer addService(BindableService bindableService) {
        return addService(bindableService, Collections.emptyList());
    }

    GrpcServer addService(BindableService bindableService, List<ServerInterceptor> interceptors);

}
