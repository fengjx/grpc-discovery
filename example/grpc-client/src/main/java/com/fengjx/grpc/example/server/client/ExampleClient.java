package com.fengjx.grpc.example.server.client;

import com.fengjx.grpc.client.GrpcClient;
import com.fengjx.grpc.client.ZkGrpcClient;
import com.fengjx.grpc.common.config.ZkConfiguration;
import com.fengjx.grpc.example.proto.helloworld.GreeterServiceGrpc;
import com.fengjx.grpc.example.proto.helloworld.HelloReply;
import com.fengjx.grpc.example.proto.helloworld.HelloRequest;
import com.fengjx.grpc.example.server.proto.config.ZkConfig;

import java.util.concurrent.TimeUnit;

/**
 * @author fengjianxin
 */
public class ExampleClient {


    public static void main(String[] args) throws Exception {
        ZkConfiguration zkConfig = ZkConfig.EXAMPLE_CONFIG;
        GrpcClient grpcClient = new ZkGrpcClient(zkConfig);
        grpcClient.satrt();
        GreeterServiceGrpc.GreeterServiceBlockingStub greeterService = grpcClient.newStub("hello-world", GreeterServiceGrpc.GreeterServiceBlockingStub.class);
        HelloRequest request = HelloRequest.newBuilder().setName("fengjx").build();
        while (true) {
            try {
                HelloReply helloReply = greeterService.sayHello(request);
                System.out.println(helloReply.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            TimeUnit.SECONDS.sleep(1);
        }
    }


}
