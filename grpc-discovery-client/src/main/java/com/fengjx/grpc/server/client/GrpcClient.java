package com.fengjx.grpc.server.client;

import io.grpc.Channel;
import io.grpc.ClientInterceptor;

import java.util.List;

/**
 * @author fengjianxin
 */
public interface GrpcClient {

    void satrt() throws Exception;
    void destroy() throws Exception;

    Channel newBlockingStub(String name);

    Channel newStub(String name);

    Channel newFutureStub(String name);

    Channel newBlockingStub(String name, List<ClientInterceptor> interceptors);

    Channel newStub(String name, List<ClientInterceptor> interceptors);

    Channel newFutureStub(String name, List<ClientInterceptor> interceptors);
}
