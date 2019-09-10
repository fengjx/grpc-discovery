package com.fengjx.grpc.common.discovery;

import com.fengjx.grpc.common.utils.NetworkUtils;
import lombok.Setter;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fengjianxin
 */
@Setter
public class DefaultServiceInstance implements ServiceInstance {


    private String serviceId;
    private String host = "0.0.0.0";
    private String ip = NetworkUtils.getLocalInnerIp();
    private int port;
    private Map<String, Object> metadata = new HashMap<>();


    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public int getPort() {
        return port;
    }


    public void setPort(int port) {
        if (port == 0) {
            this.port = NetworkUtils.getUsableLocalPort();
        } else {
            this.port = port;
        }
    }

    @Override
    public URI getUri() {
        return getUri(this);
    }

    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public static URI getUri(ServiceInstance instance) {
        String uri = String.format("grpc://%s:%s", instance.getHost(), instance.getPort());
        return URI.create(uri);
    }
}
