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
import jakarta.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

public class HelidonContextInjector {

    private HelidonContextInjector() {
    }

    /**
     * Performs field‑level injection of JAX‑RS {@code @Context}‑annotated members on the supplied
     * {@code instance}.  The method inspects every declared field of the object's runtime class,
     * looks for the {@link javax.ws.rs.core.Context} annotation and, if present, attempts to set
     * the field to an appropriate Helidon‑specific implementation based on the field type.
     *
     * <p>If a field is annotated with {@code @Context} but its type is not one of the
     * supported types above, an {@link java.lang.UnsupportedOperationException} is thrown.
     *
     * <p>All fields are made {@code accessible} via reflection before the injection is performed.
     * Any reflection‑related problems (illegal arguments, illegal access, etc.) are wrapped in a
     * {@link RuntimeException} and re‑thrown.
     *
     * @param instance the object whose {@code @Context} fields should be populated; must not be
     *                 {@code null}
     * @param context  the Helidon request context that provides the underlying
     *                 {@link io.helidon.webserver.http.ServerRequest}
     *                 used to create concrete implementations; must not be {@code null}
     * @throws NullPointerException          if {@code instance} or {@code context} is {@code null}
     * @throws UnsupportedOperationException if a {@code @Context} field has an unsupported type
     * @throws RuntimeException              if reflection fails while setting a field
     */
    public static void inject(Object instance, HelidonContainerRequestContext context) {
        Class<?> clazz = instance.getClass();

        // field injection of @Context
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Annotation annotation = field.getAnnotation(Context.class);
            if (annotation != null) {
                Class<?> fieldType = field.getType();
                try {
                    if (fieldType.equals(UriInfo.class)) {
                        field.set(instance, new HelidonUriInfo(context.getServerRequest()));
                    } else if (fieldType.equals(ResourceInfo.class)) {
                        field.set(instance, context.getResourceInfo());
                    } else if (fieldType.equals(HttpServletRequest.class)) {
                        field.set(instance, new HelidonHttpServletRequest(context.getServerRequest()));
                    } else {
                        throw new UnsupportedOperationException("@Context annotation is not supported for " + fieldType);
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Invokes the first {@code @PostConstruct} method (if any) declared on the supplied object.
     *
     * <p>This helper is used by Helidon’s JAX‑RS runtime to perform lifecycle callbacks on
     * resource or provider instances after all {@code @Context} fields have been injected.
     *
     * <p>If the class does not declare a matching {@code @PostConstruct} method the call
     * is a no‑op.
     *
     * @param instance the object whose lifecycle callback should be executed; must not be
     *                 {@code null}
     * @throws NullPointerException if {@code instance} or {@code context} is {@code null}
     * @throws RuntimeException     if the {@code @PostConstruct} method throws an
     *                              exception or if reflection fails
     */
    public static void postConstruct(Object instance) {
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
