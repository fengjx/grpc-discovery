
package com.fengjx.grpc.client;

import io.grpc.ClientInterceptor;
import io.grpc.stub.AbstractStub;

import java.util.Collections;
import java.util.List;

/**
 * @author fengjianxin
 */
public interface GrpcClient {

    void satrt() throws Exception;

    void destroy() throws Exception;

    default <T extends AbstractStub<T>> T newStub(String serviceId, Class<T> cls) {
        return newStub(serviceId, cls, Collections.emptyList());
    }

    <T extends AbstractStub<T>> T newStub(String serviceId, Class<T> cls, List<ClientInterceptor> interceptors);

    default <T> T newAnyTypeStub(String serviceId, Class<T> cls) {
        return newAnyTypeStub(serviceId, cls, Collections.emptyList());
    }

    <T> T newAnyTypeStub(String serviceId, Class<T> cls, List<ClientInterceptor> interceptors);

}
