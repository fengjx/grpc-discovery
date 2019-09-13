
package com.fengjx.grpc.client.resolver;

import com.fengjx.grpc.common.constant.DiscoveryConsts;
import com.fengjx.grpc.common.utils.NetworkUtils;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

import javax.annotation.concurrent.GuardedBy;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * @author fengjianxin
 */
@Slf4j
public class ZkNameResolver extends NameResolver implements ZkEventCallback {


    private final String serviceId;
    private final CuratorFramework client;

    @GuardedBy("this")
    private Listener listener;
    @GuardedBy("this")
    private boolean shutdown = false;

    public ZkNameResolver(String serviceId, CuratorFramework client) {
        this.serviceId = serviceId;
        this.client = client;
    }

    @Override
    public String getServiceAuthority() {
        return serviceId;
    }

    @Override
    public final synchronized void start(final Listener listener) {
        checkState(this.listener == null, "already started");
        this.listener = checkNotNull(listener, "listener");
        resolve();
        watch();
    }

    private void watch() {
        PathChildrenCache watcher = new PathChildrenCache(client, getPath(), false);
        watcher.getListenable().addListener(this);
        try {
            watcher.start();
        } catch (Exception e) {
            log.error("gRPC server watch error, serviceId: {}", serviceId, e);
        }
    }

    @Override
    public final synchronized void refresh() {
        if (this.listener != null) {
            resolve();
        }
    }

    private void resolve() {
        log.debug("resolve for gRPC servers {}", serviceId);
        if (this.shutdown) {
            return;
        }
        try {
            client.getChildren().inBackground(this).forPath(getPath());
        } catch (Exception e) {
            log.error("resolve error, serviceId: {}", serviceId, e);
            this.listener.onError(Status.UNAVAILABLE.withCause(e));
        }
    }


    @Override
    public void shutdown() {
        this.shutdown = true;
    }

    @Override
    public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) {
        log.info("childEvent: {}", event.getType());
        resolve();
    }

    @Override
    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
        log.info("processResult CuratorEvent: {}", event);
        List<String> servers = event.getChildren();
        addServersToListener(servers);
    }

    private synchronized void addServersToListener(List<String> servers) {
        List<EquivalentAddressGroup> addrs = Lists.newArrayList();
        for (String child : servers) {
            try {
                URI uri = NetworkUtils.buildUri(DiscoveryConsts.GRPC_SCHEME, child);
                log.info("find server node for [{}], uri: {}", serviceId, uri);
                addrs.add(new EquivalentAddressGroup(new InetSocketAddress(uri.getHost(), uri.getPort()),
                        Attributes.EMPTY));
            } catch (URISyntaxException e) {
                log.error("unparsable server address: {}", child, e);
            }
        }
        // for test
        if (listener == null) {
            return;
        }

        if (addrs.size() > 0) {
            listener.onAddresses(addrs, Attributes.EMPTY);
        } else {
            log.info("No servers online. Keep looking");
            this.listener.onError(Status.UNAVAILABLE
                    .withDescription("None of the servers for " + serviceId));
        }
    }

    @VisibleForTesting
    void testWatch() {
        watch();
    }


    private String getPath() {
        return "/" + serviceId;
    }

}
