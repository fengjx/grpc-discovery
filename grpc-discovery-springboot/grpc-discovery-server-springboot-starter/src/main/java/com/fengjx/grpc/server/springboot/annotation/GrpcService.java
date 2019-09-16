
package com.fengjx.grpc.server.springboot.annotation;

import io.grpc.ServerInterceptor;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface GrpcService {

    Class<? extends ServerInterceptor>[] interceptors() default {};

    String[] interceptorNames() default {};

}
