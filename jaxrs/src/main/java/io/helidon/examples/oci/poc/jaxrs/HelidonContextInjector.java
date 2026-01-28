/*
 * Copyright (c) 2026 Oracle and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.helidon.examples.oci.poc.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;

public class HelidonContextInjector {

    private HelidonContextInjector() {
    }

    public static void inject(Object instance, HelidonContainerRequestContext context) {
        Class<?> clazz = instance.getClass();

        // field injection of @Context
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Annotation annotation  = field.getAnnotation(Context.class);
            if (annotation != null) {
                Class<?> fieldType = field.getType();
                if (fieldType.equals(UriInfo.class)) {
                    try {
                        field.set(instance, new HelidonUriInfo(context.getServerRequest()));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    throw new UnsupportedOperationException("@Context annotation is not supported");
                }
            }
        }
    }

    public static void postConstruct(Object instance, HelidonContainerRequestContext context) {
        Class<?> clazz = instance.getClass();

        // call @PostConstruct if defined
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            Annotation annotation = method.getAnnotation(PostConstruct.class);
            if (annotation != null) {
                Class<?> methodType = method.getReturnType();
                int paramCount = method.getParameterCount();
                if (methodType.equals(Void.TYPE) && paramCount == 0) {
                    try {
                        method.invoke(instance);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
