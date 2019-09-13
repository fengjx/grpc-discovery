
package com.fengjx.grpc.client.resolver;

import com.fengjx.grpc.common.constant.DiscoveryConsts;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.URI;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author fengjianxin
 */
@Slf4j
public class ZkNameResolverProvider extends NameResolverProvider {

    private final CuratorFramework client;

    public ZkNameResolverProvider(CuratorFramework client) {
        this.client = client;
    }

    @Override
    public NameResolver newNameResolver(URI targetUri, NameResolver.Args args) {
        checkState(DiscoveryConsts.DISCOVERY_SCHEME.equals(targetUri.getScheme()),
                "not support scheme type: " + targetUri.getScheme());
        final String serviceId = targetUri.getHost();
        return new ZkNameResolver(serviceId, client);
    }

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 0;
    }

    @Override
    public String getDefaultScheme() {
        return DiscoveryConsts.DISCOVERY_SCHEME;
    }
}
