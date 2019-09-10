package com.fengjx.grpc.common.discovery;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonInstanceSerializer implements InstanceSerializer {

    private Gson gson = new GsonBuilder().create();

    @Override
    public byte[] serialize(ServiceInstance instance) {
        return gson.toJson(instance).getBytes();
    }

    @Override
    public ServiceInstance deserialize(byte[] bytes) {
        return gson.fromJson(new String(bytes), ServiceInstance.class);
    }
}