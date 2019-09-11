
package com.fengjx.grpc.server.client.resolver;

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
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * @author fengjianxin
 */
@Slf4j
public class ZkNameResolver extends NameResolver implements ZkEventCallback {


    private final String name;
    private CuratorFramework client;

    @GuardedBy("this")
    private Listener listener;
    @GuardedBy("this")
    private boolean resolving;
    @GuardedBy("this")
    private boolean shutdown = false;

    public ZkNameResolver(String name, CuratorFramework client) {
        this.name = name;
        this.client = client;
        resolve();
        watch();
    }

    @Override
    public String getServiceAuthority() {
        return name;
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
            log.error("gRPC server watch error, name: {}", name, e);
        }
    }

    @Override
    public final synchronized void refresh() {
        if (this.listener != null) {
            resolve();
        }
    }

    @GuardedBy("this")
    private void resolve() {
        log.debug("resolve for {}", this.name);
        if (this.resolving || this.shutdown) {
            return;
        }
        this.resolving = true;
        try {
            client.getChildren().inBackground(this).forPath(getPath());
        } catch (Exception e) {
            log.error("resolve error, name: {}", this.name, e);
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

    private void addServersToListener(List<String> servers) {
        List<EquivalentAddressGroup> addrs = Lists.newArrayList();
        log.info("Updating server list");
        for (String child : servers) {
            try {
                URI uri = new URI("grpc://" + child);
                String host = uri.getHost();
                int port = uri.getPort();
                addrs.add(new EquivalentAddressGroup(new InetSocketAddress(host, port), Attributes.EMPTY));
            } catch (Exception e) {
                log.error("Unparsable server address: {}", child, e);
            }
        }
        if (addrs.size() > 0) {
            listener.onAddresses(addrs, Attributes.EMPTY);
        } else {
            log.info("No servers online. Keep looking");
            this.listener.onError(Status.UNAVAILABLE
                    .withDescription("None of the servers for " + name));
        }
    }


    private String getPath() {
        return "/" + name;
    }

}
