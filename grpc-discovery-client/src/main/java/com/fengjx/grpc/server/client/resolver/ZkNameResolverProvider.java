
package com.fengjx.grpc.server.client.resolver;

import com.fengjx.grpc.common.constant.DiscoveryConsts;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;

import java.net.URI;

/**
 * @author fengjianxin
 */
public class ZkNameResolverProvider extends NameResolverProvider {


    @Override
    public NameResolver newNameResolver(URI targetUri, NameResolver.Args args) {
        return super.newNameResolver(targetUri, args);
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
