
package com.fengjx.grpc.server.client;

import com.fengjx.grpc.server.client.channel.GrpcChannelFactory;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;

import java.util.List;

/**
 * @author fengjianxin
 */
public class ZkGrpcClient implements GrpcClient {

    private final GrpcChannelFactory grpcChannelFactory;

    public ZkGrpcClient(GrpcChannelFactory grpcChannelFactory) {
        this.grpcChannelFactory = grpcChannelFactory;
    }

    @Override
    public void satrt() throws Exception {

    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public Channel newBlockingStub(String name) {
        return null;
    }

    @Override
    public Channel newStub(String name) {
        return null;
    }

    @Override
    public Channel newFutureStub(String name) {
        return null;
    }

    @Override
    public Channel newBlockingStub(String name, List<ClientInterceptor> interceptors) {
        return null;
    }

    @Override
    public Channel newStub(String name, List<ClientInterceptor> interceptors) {
        return null;
    }

    @Override
    public Channel newFutureStub(String name, List<ClientInterceptor> interceptors) {
        return null;
    }


}
