
package com.fengjx.grpc.client.channel;

import io.grpc.NameResolverProvider;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

/**
 * @author fengjianxin
 */
public class ShadedNettyChannelFactory extends AbstractChannelFactory<NettyChannelBuilder> {

    public ShadedNettyChannelFactory(NameResolverProvider nameResolverProvider) {
        super(nameResolverProvider);
    }

    @Override
    protected NettyChannelBuilder newChannelBuilder(final String serviceId) {
        return NettyChannelBuilder.forTarget(buildTarget(serviceId))
                .defaultLoadBalancingPolicy(DEFAULT_LOAD_BALANCING_POLICY)
                .nameResolverFactory(nameResolverProvider).usePlaintext();
    }


}
