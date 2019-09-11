package com.fengjx.grpc.example;

import com.fengjx.grpc.common.config.ZkProperties;
import com.fengjx.grpc.common.discovery.DefaultServiceInstance;
import com.fengjx.grpc.server.registry.ServerRegistration;
import com.fengjx.grpc.server.registry.ZkServerRegistration;
import com.fengjx.grpc.server.GrpcServer;

/**
 * @author fengjianxin
 */
public class ExampleServer {

    public static void main(String[] args) throws Exception {
        ZkProperties zkPro = new ZkProperties();
        zkPro.setConnectString("localhost:2181");
        zkPro.setNamespace("grpc-example");

        ServerRegistration registration = new ZkServerRegistration(zkPro);
        DefaultServiceInstance instance = new DefaultServiceInstance();
        instance.setServiceId("hello-world");
        instance.setPort(0);

        GrpcServer grpcServer = GrpcServer.newInstance(instance).registration(registration);
        grpcServer.addService(new GreeterServiceImpl());
        grpcServer.start(true);
        Runtime.getRuntime().addShutdownHook(new Thread(grpcServer::destroy));
    }


}
