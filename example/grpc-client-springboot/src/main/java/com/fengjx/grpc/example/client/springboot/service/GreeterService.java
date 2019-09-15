
package com.fengjx.grpc.example.client.springboot.service;

import com.fengjx.grpc.example.proto.helloworld.HelloReply;
import org.springframework.stereotype.Service;

import com.fengjx.grpc.client.springboot.annotation.GrpcStub;
import com.fengjx.grpc.example.proto.helloworld.GreeterServiceGrpc;
import com.fengjx.grpc.example.proto.helloworld.HelloRequest;

/**
 * @author fengjianxin
 */
@Service
public class GreeterService {

    @GrpcStub("hello-world")
    private GreeterServiceGrpc.GreeterServiceBlockingStub blockingStub;

    public String sayHello(String name) {
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply helloReply = blockingStub.sayHello(request);
        return helloReply.getMessage();
    }

}
