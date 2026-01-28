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

import java.util.concurrent.CompletionStage;

import jakarta.ws.rs.SeBootstrap;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.EntityPart;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.Variant;
import jakarta.ws.rs.ext.RuntimeDelegate;

public class HelidonRuntimeDelegate extends RuntimeDelegate {

    @Override
    public UriBuilder createUriBuilder() {
        return null;
    }

    @Override
    public Response.ResponseBuilder createResponseBuilder() {
        return new HelidonResponse.ResponseBuilder();
    }

    @Override
    public Variant.VariantListBuilder createVariantListBuilder() {
        return null;
    }

    @Override
    public <T> T createEndpoint(Application application, Class<T> endpointType)
            throws IllegalArgumentException, UnsupportedOperationException {
        return null;
    }

    @Override
    public <T> HeaderDelegate<T> createHeaderDelegate(Class<T> type) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Link.Builder createLinkBuilder() {
        return null;
    }

    @Override
    public SeBootstrap.Configuration.Builder createConfigurationBuilder() {
        return null;
    }

    @Override
    public CompletionStage<SeBootstrap.Instance> bootstrap(Application application, SeBootstrap.Configuration configuration) {
        return null;
    }

    @Override
    public CompletionStage<SeBootstrap.Instance> bootstrap(Class<? extends Application> clazz,
                                                           SeBootstrap.Configuration configuration) {
        return null;
    }

    @Override
    public EntityPart.Builder createEntityPartBuilder(String partName) throws IllegalArgumentException {
        return null;
    }
}
