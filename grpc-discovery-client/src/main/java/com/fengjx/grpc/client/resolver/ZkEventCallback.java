package com.fengjx.grpc.client.resolver;

import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

/**
 * @author fengjianxin
 */
public interface ZkEventCallback extends PathChildrenCacheListener, BackgroundCallback {
}
