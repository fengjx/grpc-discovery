package com.fengjx.grpc.common.discovery;


public interface InstanceSerializer {

    byte[] serialize(ServiceInstance instance) throws Exception;

    ServiceInstance deserialize(byte[] bytes) throws Exception;

}