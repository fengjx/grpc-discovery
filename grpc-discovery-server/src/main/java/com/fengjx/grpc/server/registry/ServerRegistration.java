package com.fengjx.grpc.server.registry;

import com.fengjx.grpc.common.discovery.ServiceInstance;

/**
 * @author fengjianxin
 */
public interface ServerRegistration {


    void registry(ServiceInstance serviceInstance) throws Exception;


}
