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
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Variant;

public class HelidonResponse extends Response {

    private final int status;
    private final String reasonPhrase;

    private HelidonResponse(ResponseBuilder builder) {
        this.status = builder.status;
        this.reasonPhrase = builder.reasonPhrase;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public StatusType getStatusInfo() {
        return null;
    }

    @Override
    public Object getEntity() {
        return null;
    }

    @Override
    public <T> T readEntity(Class<T> entityType) {
        return null;
    }

    @Override
    public <T> T readEntity(GenericType<T> entityType) {
        return null;
    }

    @Override
    public <T> T readEntity(Class<T> entityType, Annotation[] annotations) {
        return null;
    }

    @Override
    public <T> T readEntity(GenericType<T> entityType, Annotation[] annotations) {
        return null;
    }

    @Override
    public boolean hasEntity() {
        return false;
    }

    @Override
    public boolean bufferEntity() {
        return false;
    }

    @Override
    public void close() {

    }

    @Override
    public MediaType getMediaType() {
        return null;
    }

    @Override
    public Locale getLanguage() {
        return null;
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public Set<String> getAllowedMethods() {
        return Set.of();
    }

    @Override
    public Map<String, NewCookie> getCookies() {
        return Map.of();
    }

    @Override
    public EntityTag getEntityTag() {
        return null;
    }

    @Override
    public Date getDate() {
        return null;
    }

    @Override
    public Date getLastModified() {
        return null;
    }

    @Override
    public URI getLocation() {
        return null;
    }

    @Override
    public Set<Link> getLinks() {
        return Set.of();
    }

    @Override
    public boolean hasLink(String relation) {
        return false;
    }

    @Override
    public Link getLink(String relation) {
        return null;
    }

    @Override
    public Link.Builder getLinkBuilder(String relation) {
        return null;
    }

    @Override
    public MultivaluedMap<String, Object> getMetadata() {
        return null;
    }

    @Override
    public MultivaluedMap<String, String> getStringHeaders() {
        return null;
    }

    @Override
    public String getHeaderString(String name) {
        return "";
    }

    static class ResponseBuilder extends Response.ResponseBuilder {

        private int status;
        private String reasonPhrase;

        @Override
        public Response build() {
            return new  HelidonResponse(this);
        }

        @Override
        public Response.ResponseBuilder clone() {
            return null;
        }

        @Override
        public Response.ResponseBuilder status(int status) {
            this.status = status;
            return this;
        }

        @Override
        public Response.ResponseBuilder status(int status, String reasonPhrase) {
            this.status = status;
            this.reasonPhrase = reasonPhrase;
            return this;
        }

        @Override
        public Response.ResponseBuilder entity(Object entity) {
            return null;
        }

        @Override
        public Response.ResponseBuilder entity(Object entity, Annotation[] annotations) {
            return null;
        }

        @Override
        public Response.ResponseBuilder allow(String... methods) {
            return null;
        }

        @Override
        public Response.ResponseBuilder allow(Set<String> methods) {
            return null;
        }

        @Override
        public Response.ResponseBuilder cacheControl(CacheControl cacheControl) {
            return null;
        }

        @Override
        public Response.ResponseBuilder encoding(String encoding) {
            return null;
        }

        @Override
        public Response.ResponseBuilder header(String name, Object value) {
            return null;
        }

        @Override
        public Response.ResponseBuilder replaceAll(MultivaluedMap<String, Object> headers) {
            return null;
        }

        @Override
        public Response.ResponseBuilder language(String language) {
            return null;
        }

        @Override
        public Response.ResponseBuilder language(Locale language) {
            return null;
        }

        @Override
        public Response.ResponseBuilder type(MediaType type) {
            return null;
        }

        @Override
        public Response.ResponseBuilder type(String type) {
            return null;
        }

        @Override
        public Response.ResponseBuilder variant(Variant variant) {
            return null;
        }

        @Override
        public Response.ResponseBuilder contentLocation(URI location) {
            return null;
        }

        @Override
        public Response.ResponseBuilder cookie(NewCookie... cookies) {
            return null;
        }

        @Override
        public Response.ResponseBuilder expires(Date expires) {
            return null;
        }

        @Override
        public Response.ResponseBuilder lastModified(Date lastModified) {
            return null;
        }

        @Override
        public Response.ResponseBuilder location(URI location) {
            return null;
        }

        @Override
        public Response.ResponseBuilder tag(EntityTag tag) {
            return null;
        }

        @Override
        public Response.ResponseBuilder tag(String tag) {
            return null;
        }

        @Override
        public Response.ResponseBuilder variants(Variant... variants) {
            return null;
        }

        @Override
        public Response.ResponseBuilder variants(List<Variant> variants) {
            return null;
        }

        @Override
        public Response.ResponseBuilder links(Link... links) {
            return null;
        }

        @Override
        public Response.ResponseBuilder link(URI uri, String rel) {
            return null;
        }

        @Override
        public Response.ResponseBuilder link(String uri, String rel) {
            return null;
        }
    }
}
