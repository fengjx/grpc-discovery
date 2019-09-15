package com.fengjx.grpc.example.server;

import com.fengjx.grpc.common.config.ZkConfiguration;
import com.fengjx.grpc.common.discovery.DefaultServiceInstance;
import com.fengjx.grpc.example.server.proto.config.ZkConfig;
import com.fengjx.grpc.server.registry.ServerRegistration;
import com.fengjx.grpc.server.registry.ZkServerRegistration;
import com.fengjx.grpc.server.server.GrpcServer;
import com.fengjx.grpc.server.server.ZkGrpcServer;

/**
 * @author fengjianxin
 */
public class ExampleServer {

    public static void main(String[] args) throws Exception {
        ZkConfiguration zkConfig = ZkConfig.EXAMPLE_CONFIG;

        ServerRegistration registration = new ZkServerRegistration(zkConfig);
        DefaultServiceInstance instance = new DefaultServiceInstance();
        instance.setServiceId("hello-world");
        instance.setPort(0);

        GrpcServer grpcServer = new ZkGrpcServer(instance).registration(registration);

        grpcServer.addService(new GreeterServiceImpl());
        grpcServer.start(true);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                grpcServer.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }


}
