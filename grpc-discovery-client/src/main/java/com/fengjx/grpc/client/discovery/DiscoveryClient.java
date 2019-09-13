
package com.fengjx.grpc.client.discovery;

import com.fengjx.grpc.common.discovery.ServiceInstance;

import java.util.List;

/**
 * @author fengjianxin
 */
public interface DiscoveryClient {

    String description();

    List<ServiceInstance> getServices(String serviceId);

    List<String> getServiceIds();

}
