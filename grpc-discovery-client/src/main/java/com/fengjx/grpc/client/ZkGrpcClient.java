
package com.fengjx.grpc.client;

import cn.hutool.core.util.ReflectUtil;
import com.fengjx.grpc.common.config.ZkConfiguration;
import com.fengjx.grpc.common.utils.ZkUtils;
import com.fengjx.grpc.client.channel.GrpcChannelFactory;
import com.fengjx.grpc.client.channel.ShadedNettyChannelFactory;
import com.fengjx.grpc.client.resolver.ZkNameResolverProvider;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.NameResolverProvider;
import io.grpc.stub.AbstractStub;
import org.apache.curator.framework.CuratorFramework;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author fengjianxin
 */
public class ZkGrpcClient implements GrpcClient {

    private boolean started = false;
    private GrpcChannelFactory grpcChannelFactory;
    private NameResolverProvider nameResolverProvider;
    private ZkConfiguration zkConfiguration;
    private CuratorFramework client;

    public ZkGrpcClient(ZkConfiguration zkConfiguration) {
        this.zkConfiguration = zkConfiguration;
    }

    public ZkGrpcClient(GrpcChannelFactory grpcChannelFactory) {
        this.grpcChannelFactory = grpcChannelFactory;
    }

    public synchronized void grpcChannelFactory(GrpcChannelFactory grpcChannelFactory) {
        checkState(!started, "zkGrpcClient started, can not set grpcChannelFactory");
        this.grpcChannelFactory = grpcChannelFactory;
    }

    @Override
    public void satrt() throws Exception {
        init();
    }

    @Override
    public void destroy() throws Exception {
        if (client != null) {
            client.close();
        }
    }

    @Override
    public <T extends AbstractStub<T>> T newStub(String serviceId, Class<T> cls, List<ClientInterceptor> interceptors) {
        Channel channel = grpcChannelFactory.createChannel(serviceId, interceptors);
        return ReflectUtil.newInstance(cls, channel);
    }


    private synchronized void init() throws InterruptedException {
        client = ZkUtils.createCuratorFramework(zkConfiguration);
        if (nameResolverProvider == null) {
            nameResolverProvider = new ZkNameResolverProvider(client);
        }
        if (grpcChannelFactory == null) {
            grpcChannelFactory = new ShadedNettyChannelFactory(nameResolverProvider);
        }
        started = true;
    }

}
