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

import java.util.List;

import io.helidon.common.types.ElementSignature;
import io.helidon.common.types.TypeName;
import io.helidon.common.types.TypedElementInfo;
import io.helidon.examples.oci.poc.jaxrs.HelidonContainerRequestContext;
import io.helidon.examples.oci.poc.jaxrs.HelidonContextInjector;
import io.helidon.service.registry.InterceptionContext;
import io.helidon.service.registry.Service;
import io.helidon.webserver.http.HttpEntryPoint;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;

/**
 * NOTE: this interceptor should be code generated for method
 * {@link EchoEndpoint#twice(String, String)}.
 */
@SuppressWarnings("deprecation")
@Service.Singleton
class EchoEndpointTwiceInterceptor implements HttpEntryPoint.Interceptor {

    private static final TypeName STRING_TYPENAME = TypeName.create(String.class.getName());
    private static final ElementSignature ELEMENT_SIGNATURE = ElementSignature.createMethod(
            STRING_TYPENAME, "twice", List.of(STRING_TYPENAME, STRING_TYPENAME));

    @Override
    public void proceed(InterceptionContext interceptionContext,
                        Chain chain,
                        ServerRequest request,
                        ServerResponse response) throws Exception {
        TypedElementInfo typedElementInfo = interceptionContext.elementInfo();

        // check if it applies to this element
        if (typedElementInfo.signature().equals(ELEMENT_SIGNATURE) &&
                typedElementInfo.hasAnnotation(TypeName.create(Authorized.class))) {
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
    }
}
