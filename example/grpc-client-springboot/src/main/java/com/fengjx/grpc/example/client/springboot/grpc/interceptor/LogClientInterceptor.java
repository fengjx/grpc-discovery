
package com.fengjx.grpc.example.client.springboot.grpc.interceptor;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author fengjianxin
 */
@Slf4j
@Component
public class LogClientInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions, Channel next) {
        log.info("method: {}", method.getFullMethodName());
        return next.newCall(method, callOptions);
    }
}
