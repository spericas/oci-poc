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

import io.helidon.common.types.TypeName;
import io.helidon.common.types.TypedElementInfo;
import io.helidon.examples.oci.poc.jaxrs.HelidonContainerRequestContext;
import io.helidon.examples.oci.poc.jaxrs.HelidonContextInjector;
import io.helidon.service.registry.InterceptionContext;
import io.helidon.service.registry.Service;
import io.helidon.webserver.http.HttpEntryPoint;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;

@SuppressWarnings("deprecation")
@Service.Singleton
class AuthorizationInterceptor implements HttpEntryPoint.Interceptor {

    @Override
    public void proceed(InterceptionContext interceptionContext,
                        Chain chain,
                        ServerRequest request,
                        ServerResponse response) {
        try {
            TypedElementInfo typedElementInfo = interceptionContext.elementInfo();
            if (typedElementInfo.hasAnnotation(TypeName.create(Authorized.class))) {
                // instantiate JAX-RS filter
                AuthorizationFilter filter = new AuthorizationFilter();
                HelidonContainerRequestContext context = new HelidonContainerRequestContext(request);

                // prepare instance
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
            }

            // next interceptor
            chain.proceed(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
