
package com.fengjx.grpc.server.client.channel;

import io.grpc.Channel;
import io.grpc.ClientInterceptor;

import java.util.Collections;
import java.util.List;

/**
 * @author fengjianxin
 */
public interface GrpcChannelFactory {

    default Channel createChannel(final String name) {
        return createChannel(name, Collections.emptyList());
    }

    Channel createChannel(String name, List<ClientInterceptor> interceptors);

}
