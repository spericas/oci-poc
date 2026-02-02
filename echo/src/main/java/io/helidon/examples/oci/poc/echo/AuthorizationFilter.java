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
package io.helidon.examples.oci.poc.echo;

import java.util.List;

import io.helidon.http.HeaderNames;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import static java.lang.System.Logger.Level;

public class AuthorizationFilter implements ContainerRequestFilter {
    private static final System.Logger LOGGER = System.getLogger(AuthorizationFilter.class.getName());

    @Context
    private UriInfo uriInfo;

    public AuthorizationFilter() {
    }

    @PostConstruct
    public void init() {
        validate();         // injections available here
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        MultivaluedMap<String, String> headers = requestContext.getHeaders();
        String v = headers.getFirst(HeaderNames.AUTHORIZATION.defaultCase());
        if ("helidon".equals(v)) {
            LOGGER.log(Level.DEBUG, "Authorizing access '" + requestContext.getUriInfo().getPath() + "'");
            headers.put("User", List.of(v));        // adds User header
        } else {
            LOGGER.log(Level.DEBUG, "Rejecting access '" + requestContext.getUriInfo().getPath() + "'");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private void validate() {
        if (uriInfo == null) {
            throw new IllegalStateException("Missing UriInfo");
        }
    }
}
