package com.fengjx.grpc.example;

import com.fengjx.grpc.helloworld.GreeterServiceGrpc;
import com.fengjx.grpc.helloworld.HelloReply;
import com.fengjx.grpc.helloworld.HelloRequest;
import io.grpc.stub.StreamObserver;

/**
 * @author fengjianxin
 */
public class GreeterServiceImpl extends GreeterServiceGrpc.GreeterServiceImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply resp = HelloReply.newBuilder().setMessage("hello: " + request.getName()).build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }
}
