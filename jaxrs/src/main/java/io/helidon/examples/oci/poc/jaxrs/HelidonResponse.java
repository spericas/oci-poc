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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getEntity() {
        return null;
    }

    @Override
    public <T> T readEntity(Class<T> entityType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T readEntity(GenericType<T> entityType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T readEntity(Class<T> entityType, Annotation[] annotations) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T readEntity(GenericType<T> entityType, Annotation[] annotations) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasEntity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean bufferEntity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() {
    }

    @Override
    public MediaType getMediaType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Locale getLanguage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getLength() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<String> getAllowedMethods() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, NewCookie> getCookies() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EntityTag getEntityTag() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Date getDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Date getLastModified() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public URI getLocation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<Link> getLinks() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasLink(String relation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Link getLink(String relation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Link.Builder getLinkBuilder(String relation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MultivaluedMap<String, Object> getMetadata() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MultivaluedMap<String, String> getStringHeaders() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getHeaderString(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    static class ResponseBuilder extends Response.ResponseBuilder {

        private int status;
        private String reasonPhrase;

        @Override
        public Response build() {
            return new HelidonResponse(this);
        }

        @Override
        public Response.ResponseBuilder clone() {
            throw new UnsupportedOperationException("Not supported yet.");
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
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder entity(Object entity, Annotation[] annotations) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder allow(String... methods) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder allow(Set<String> methods) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder cacheControl(CacheControl cacheControl) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder encoding(String encoding) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder header(String name, Object value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder replaceAll(MultivaluedMap<String, Object> headers) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder language(String language) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder language(Locale language) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder type(MediaType type) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder type(String type) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder variant(Variant variant) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder contentLocation(URI location) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder cookie(NewCookie... cookies) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder expires(Date expires) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder lastModified(Date lastModified) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder location(URI location) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder tag(EntityTag tag) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder tag(String tag) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder variants(Variant... variants) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder variants(List<Variant> variants) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder links(Link... links) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder link(URI uri, String rel) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Response.ResponseBuilder link(String uri, String rel) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
