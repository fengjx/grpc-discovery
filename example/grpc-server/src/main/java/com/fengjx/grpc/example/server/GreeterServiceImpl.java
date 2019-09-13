package com.fengjx.grpc.example.server;


import com.fengjx.grpc.example.proto.helloworld.GreeterServiceGrpc;
import com.fengjx.grpc.example.proto.helloworld.HelloReply;
import com.fengjx.grpc.example.proto.helloworld.HelloRequest;
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
