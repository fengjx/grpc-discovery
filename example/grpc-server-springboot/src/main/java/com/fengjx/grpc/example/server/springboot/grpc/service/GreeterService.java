
package com.fengjx.grpc.example.server.springboot.grpc.service;


import com.fengjx.grpc.example.proto.helloworld.GreeterServiceGrpc;
import com.fengjx.grpc.example.proto.helloworld.HelloReply;
import com.fengjx.grpc.example.proto.helloworld.HelloRequest;
import com.fengjx.grpc.example.server.springboot.grpc.interceptor.LogServerInterceptor;
import com.fengjx.grpc.server.springboot.annotation.GrpcService;

import io.grpc.stub.StreamObserver;

/**
 * @author fengjianxin
 */
@GrpcService(interceptors = LogServerInterceptor.class)
public class GreeterService extends GreeterServiceGrpc.GreeterServiceImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply resp = HelloReply.newBuilder().setMessage("springboot => hello: " + request.getName()).build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }
}
