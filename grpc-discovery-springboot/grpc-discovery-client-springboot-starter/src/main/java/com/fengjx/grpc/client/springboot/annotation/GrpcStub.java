
package com.fengjx.grpc.client.springboot.annotation;

import io.grpc.ClientInterceptor;

import java.lang.annotation.*;


@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
/**
 * @author fengjianxin
 */
public @interface GrpcStub {

    /**
     * grpc server register serviceId
     */
    String value();

    /**
     * inject by spring bean type
     */
    Class<? extends ClientInterceptor>[] interceptors() default {};

    /**
     * inject by spring bean name
     */
    String[] interceptorNames() default {};

}
