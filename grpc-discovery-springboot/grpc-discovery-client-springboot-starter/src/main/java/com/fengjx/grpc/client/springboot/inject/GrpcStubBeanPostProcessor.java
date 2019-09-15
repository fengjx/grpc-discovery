
package com.fengjx.grpc.client.springboot.inject;

import com.fengjx.grpc.client.GrpcClient;
import com.fengjx.grpc.client.springboot.annotation.GrpcStub;
import com.google.common.collect.Lists;
import io.grpc.ClientInterceptor;
import io.grpc.stub.AbstractStub;
import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * @author fengjianxin
 */
public class GrpcStubBeanPostProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;
    private final GrpcClient grpcClient;

    public GrpcStubBeanPostProcessor(final ApplicationContext applicationContext, GrpcClient grpcClient) {
        this.applicationContext = requireNonNull(applicationContext, "applicationContext");
        this.grpcClient = requireNonNull(grpcClient, "grpcClient");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        do {
            for (final Field field : clazz.getDeclaredFields()) {
                final GrpcStub annotation = AnnotationUtils.findAnnotation(field, GrpcStub.class);
                if (annotation != null) {
                    ReflectionUtils.makeAccessible(field);
                    ReflectionUtils.setField(field, bean, processInjectionPoint(field, field.getType(), annotation));
                }
            }
            for (final Method method : clazz.getDeclaredMethods()) {
                final GrpcStub annotation = AnnotationUtils.findAnnotation(method, GrpcStub.class);
                if (annotation != null) {
                    final Class<?>[] paramTypes = method.getParameterTypes();
                    if (paramTypes.length != 1) {
                        throw new BeanDefinitionStoreException(
                                "Method " + method + " doesn't have exactly one parameter.");
                    }
                    ReflectionUtils.makeAccessible(method);
                    ReflectionUtils.invokeMethod(method, bean,
                            processInjectionPoint(method, paramTypes[0], annotation));
                }
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        return bean;
    }

    private <T> T processInjectionPoint(final Member injectionTarget,
            final Class<T> injectionType,
            final GrpcStub annotation) {
        if (!AbstractStub.class.isAssignableFrom(injectionType)) {
            throw new InvalidPropertyException(injectionTarget.getDeclaringClass(), injectionTarget.getName(),
                    "Unsupported type " + injectionType.getName());
        }
        final List<ClientInterceptor> interceptors = interceptorsFromAnnotation(annotation);
        final String serviceId = annotation.value();
        return grpcClient.newAnyTypeStub(serviceId, injectionType, interceptors);
    }

    private List<ClientInterceptor> interceptorsFromAnnotation(final GrpcStub annotation) throws BeansException {
        final List<ClientInterceptor> list = Lists.newArrayList();
        for (final Class<? extends ClientInterceptor> interceptorClass : annotation.interceptors()) {
            final ClientInterceptor clientInterceptor;
            if (this.applicationContext.getBeanNamesForType(ClientInterceptor.class).length > 0) {
                clientInterceptor = this.applicationContext.getBean(interceptorClass);
            } else {
                try {
                    clientInterceptor = interceptorClass.getConstructor().newInstance();
                } catch (final Exception e) {
                    throw new BeanCreationException("Failed to create interceptor instance", e);
                }
            }
            list.add(clientInterceptor);
        }
        for (final String interceptorName : annotation.interceptorNames()) {
            list.add(this.applicationContext.getBean(interceptorName, ClientInterceptor.class));
        }
        return list;
    }

}
