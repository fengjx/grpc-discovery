
package com.fengjx.grpc.server.client.resolver;

import io.grpc.NameResolver;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.utils.ZKPaths;

import javax.annotation.concurrent.GuardedBy;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * @author fengjianxin
 */
@Slf4j
public class ZkNameResolver extends NameResolver implements PathChildrenCacheListener {


    private final String name;
    private CuratorFramework client;

    @GuardedBy("this")
    private Listener listener;
    @GuardedBy("this")
    private boolean resolving;
    @GuardedBy("this")
    private boolean shutdown;

    public ZkNameResolver(String name, CuratorFramework client) {
        this.name = name;
        this.client = client;
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
        PathChildrenCache watcher = new PathChildrenCache(client, "/" + name, false);
        watcher.getListenable().addListener(this);
    }

    @Override
    public final synchronized void refresh() {
        if (this.listener != null) {
            resolve();
        }
    }

    @GuardedBy("this")
    private void resolve() {
        log.debug("Scheduled resolve for {}", this.name);
        if (this.resolving || this.shutdown) {
            return;
        }
        this.resolving = true;

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) {
        switch (event.getType()) {
            case CHILD_ADDED: {
                log.info("Node added: {}", ZKPaths.getNodeFromPath(event.getData().getPath()));
                break;
            }

            case CHILD_UPDATED: {
                log.info("Node changed: {}", ZKPaths.getNodeFromPath(event.getData().getPath()));
                break;
            }

            case CHILD_REMOVED: {
                log.info("Node removed: {}" + ZKPaths.getNodeFromPath(event.getData().getPath()));
                break;
            }
        }
    }


}
