package com.fengjx.grpc.client.springboot.annotation;

import io.grpc.ClientInterceptor;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GrpcStub {

    String value();

    Class<? extends ClientInterceptor>[] interceptors() default {};

}
