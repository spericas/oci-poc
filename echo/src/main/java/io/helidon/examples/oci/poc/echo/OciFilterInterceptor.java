/*
 * Copyright (c) 2023 Oracle and/or its affiliates.
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
package io.helidon.examples.oci.poc.echo;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import io.helidon.common.types.TypeName;
import io.helidon.common.types.TypedElementInfo;
import io.helidon.examples.oci.poc.jaxrs.HelidonContainerRequestContext;
import io.helidon.examples.oci.poc.jaxrs.HelidonContextInjector;
import io.helidon.http.Status;
import io.helidon.service.registry.InterceptionContext;
import io.helidon.service.registry.Service;
import io.helidon.webserver.http.HttpEntryPoint;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;

import jakarta.ws.rs.container.ContainerRequestFilter;

@SuppressWarnings("deprecation")
@Service.Singleton
class OciFilterInterceptor implements HttpEntryPoint.Interceptor {

    /**
     * Maps an annotation to a list of JAX-RS filter classes that must be called
     * to process a request. This configuration should be external.
     */
    static Map<Class<? extends Annotation>, List<Class<? extends ContainerRequestFilter>>> FILTER_MAP =
            Map.of(Authorized.class, List.of(AuthorizationFilter.class));

    @Override
    public void proceed(InterceptionContext interceptionContext,
                        Chain chain,
                        ServerRequest request,
                        ServerResponse response) {
        try {
            TypedElementInfo typedElementInfo = interceptionContext.elementInfo();
            HelidonContainerRequestContext context = new HelidonContainerRequestContext(request);

            // apply filters for each annotation
            FILTER_MAP.forEach((annotation, filterClasses) -> {
                if (typedElementInfo.hasAnnotation(TypeName.create(annotation))) {
                    for (Class<? extends ContainerRequestFilter> filterClass : filterClasses) {
                       try {
                           // prepare filter instance
                           ContainerRequestFilter filter = filterClass.getDeclaredConstructor().newInstance();
                           HelidonContextInjector.inject(filter, context);
                           HelidonContextInjector.postConstruct(filter, context);

                           // invoke filter
                           filter.filter(context);

                           // proceed based on the filter outcome
                           if (context.isAborted()) {
                               String msg = context.getAbortMessage();
                               response.status(context.getAbortStatus()).send(msg != null ? msg : "");
                               return;
                           }

                           // next interceptor
                           chain.proceed(request, response);
                       } catch (Exception e) {
                           response.status(Status.INTERNAL_SERVER_ERROR_500).send(e.getMessage());
                           return;
                       }
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
