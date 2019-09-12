
package com.fengjx.grpc.server.client.channel;

import com.fengjx.grpc.common.constant.DiscoveryConsts;
import com.fengjx.grpc.common.utils.NetworkUtils;
import io.grpc.*;
import io.grpc.netty.NettyChannelBuilder;

import javax.annotation.concurrent.GuardedBy;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author fengjianxin
 */
public class NettyChannelFactory implements GrpcChannelFactory {

    private static final String DEFAULT_LOAD_BALANCING_POLICY = "round_robin";
    private static final Duration DEFAULT_KEEP_ALIVE_TIME = Duration.of(60, ChronoUnit.SECONDS);
    private static final Duration DEFAULT_KEEP_ALIVE_TIMEOUT = Duration.of(20, ChronoUnit.SECONDS);
    private static final boolean DEFAULT_KEEP_ALIVE_WITHOUT_CALLS = false;

    private final NameResolverProvider nameResolverProvider;

    @GuardedBy("this")
    private final Map<String, ManagedChannel> channels = new ConcurrentHashMap<>();

    public NettyChannelFactory(NameResolverProvider nameResolverProvider) {
        this.nameResolverProvider = nameResolverProvider;
    }

    @Override
    public Channel createChannel(String serviceId, List<ClientInterceptor> interceptors) {
        return channels.computeIfAbsent(serviceId, this::newManagedChannel);
    }

    private ManagedChannel newManagedChannel(final String serviceId) {
        final ManagedChannelBuilder builder = newChannelBuilder(serviceId);
        configure(builder);
        return builder.build();
    }


    private NettyChannelBuilder newChannelBuilder(final String serviceId) {
        return NettyChannelBuilder.forTarget(NetworkUtils.buildUriString(DiscoveryConsts.DISCOVERY_SCHEME, serviceId))
                .defaultLoadBalancingPolicy(DEFAULT_LOAD_BALANCING_POLICY)
                .nameResolverFactory(nameResolverProvider);
    }

    private void configure(final ManagedChannelBuilder builder) {
        configureKeepAlive(builder);
    }

    private void configureKeepAlive(final ManagedChannelBuilder builder) {
        builder.keepAliveTime(DEFAULT_KEEP_ALIVE_TIME.toNanos(), TimeUnit.NANOSECONDS)
                .keepAliveTimeout(DEFAULT_KEEP_ALIVE_TIMEOUT.toNanos(), TimeUnit.NANOSECONDS)
                .keepAliveWithoutCalls(DEFAULT_KEEP_ALIVE_WITHOUT_CALLS);
    }

}
