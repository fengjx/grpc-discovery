
package com.fengjx.grpc.server.client.resolver;

import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;

import java.net.URI;

/**
 * @author fengjianxin
 */
public class ZkNameResolverProvider extends NameResolverProvider {


    public static final String DEFAULT_SCHEME = "zk";



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
        return DEFAULT_SCHEME;
    }
}
