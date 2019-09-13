
package com.fengjx.grpc.client.channel;

import com.fengjx.grpc.common.constant.DiscoveryConsts;
import com.fengjx.grpc.common.utils.NetworkUtils;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;

import java.util.Collections;
import java.util.List;

/**
 * @author fengjianxin
 */
public interface GrpcChannelFactory {

    default Channel createChannel(String serviceId) {
        return createChannel(serviceId, Collections.emptyList());
    }

    default String buildTarget(String serviceId) {
        return NetworkUtils.buildUriString(DiscoveryConsts.DISCOVERY_SCHEME, serviceId);
    }

    Channel createChannel(String name, List<ClientInterceptor> interceptors);


}
