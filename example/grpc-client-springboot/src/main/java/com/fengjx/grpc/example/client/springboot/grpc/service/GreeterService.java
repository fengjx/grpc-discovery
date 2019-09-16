
package com.fengjx.grpc.example.client.springboot.grpc.service;

import org.springframework.stereotype.Service;

import com.fengjx.grpc.client.springboot.annotation.GrpcStub;
import com.fengjx.grpc.example.client.springboot.grpc.interceptor.LogClientInterceptor;
import com.fengjx.grpc.example.proto.helloworld.GreeterServiceGrpc;
import com.fengjx.grpc.example.proto.helloworld.HelloReply;
import com.fengjx.grpc.example.proto.helloworld.HelloRequest;

/**
 * @author fengjianxin
 */
@Service
public class GreeterService {

    @GrpcStub(value = "hello-world", interceptors = LogClientInterceptor.class)
    private GreeterServiceGrpc.GreeterServiceBlockingStub blockingStub;

    public String sayHello(String name) {
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply helloReply = blockingStub.sayHello(request);
        return helloReply.getMessage();
    }

}
