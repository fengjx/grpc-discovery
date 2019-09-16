
package com.fengjx.grpc.client.channel;

import cn.hutool.core.collection.CollUtil;
import io.grpc.*;

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
public abstract class AbstractChannelFactory<T extends ManagedChannelBuilder<T>> implements GrpcChannelFactory {

    static final String DEFAULT_LOAD_BALANCING_POLICY = "round_robin";
    private static final Duration DEFAULT_KEEP_ALIVE_TIME = Duration.of(60, ChronoUnit.SECONDS);
    private static final Duration DEFAULT_KEEP_ALIVE_TIMEOUT = Duration.of(20, ChronoUnit.SECONDS);
    private static final boolean DEFAULT_KEEP_ALIVE_WITHOUT_CALLS = false;

    @GuardedBy("this")
    private final Map<String, ManagedChannel> channels = new ConcurrentHashMap<>();

    protected final NameResolverProvider nameResolverProvider;

    public AbstractChannelFactory(NameResolverProvider nameResolverProvider) {
        this.nameResolverProvider = nameResolverProvider;
    }

    @Override
    public Channel createChannel(String serviceId, List<ClientInterceptor> interceptors) {
        ManagedChannel managedChannel = channels.computeIfAbsent(serviceId, this::newManagedChannel);
        if (CollUtil.isNotEmpty(interceptors)) {
            return ClientInterceptors.interceptForward(managedChannel, interceptors);
        }
        return managedChannel;
    }

    private ManagedChannel newManagedChannel(final String serviceId) {
        final ManagedChannelBuilder builder = newChannelBuilder(serviceId);
        configure(builder);
        return builder.build();
    }

    private void configure(final ManagedChannelBuilder builder) {
        configureKeepAlive(builder);
    }

    private void configureKeepAlive(final ManagedChannelBuilder builder) {
        builder.keepAliveTime(DEFAULT_KEEP_ALIVE_TIME.toNanos(), TimeUnit.NANOSECONDS)
                .keepAliveTimeout(DEFAULT_KEEP_ALIVE_TIMEOUT.toNanos(), TimeUnit.NANOSECONDS)
                .keepAliveWithoutCalls(DEFAULT_KEEP_ALIVE_WITHOUT_CALLS);
    }


    protected abstract T newChannelBuilder(String serviceId);

}
