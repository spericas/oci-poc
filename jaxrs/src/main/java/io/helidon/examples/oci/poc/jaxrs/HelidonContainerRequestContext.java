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

import java.io.InputStream;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.helidon.webserver.http.ServerRequest;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;

/**
 * Wrapper that adapts Helidon's ServerRequest to JAX-RS ContainerRequestContext.
 *
 * <p>For post-matching filters, this context also provides access to {@link ResourceInfo}
 * which contains information about the matched resource method.
 */
public class HelidonContainerRequestContext implements ContainerRequestContext {

    /** Property key for storing ResourceInfo in the context. */
    public static final String RESOURCE_INFO_PROPERTY = "jakarta.ws.rs.container.ResourceInfo";

    private final ServerRequest request;
    private final HelidonUriInfo uriInfo;
    private final HelidonHttpHeaders httpHeaders;
    private final Map<String, Object> properties = new HashMap<>();
    private SecurityContext securityContext;
    private ResourceInfo resourceInfo;
    private boolean aborted = false;
    private int abortStatus = 0;
    private String abortMessage;
    private InputStream entityStream;

    public HelidonContainerRequestContext(ServerRequest request) {
        this.request = request;
        this.uriInfo = new HelidonUriInfo(request);
        this.httpHeaders = new HelidonHttpHeaders(request);
    }

    /**
     * Create a request context with ResourceInfo for post-matching filters.
     *
     * @param request the server request
     * @param resourceInfo the matched resource info
     */
    public HelidonContainerRequestContext(ServerRequest request, ResourceInfo resourceInfo) {
        this(request);
        this.resourceInfo = resourceInfo;
        // Also store in properties for filters that access it via getProperty()
        this.properties.put(RESOURCE_INFO_PROPERTY, resourceInfo);
    }

    /**
     * Get the ResourceInfo for the matched resource method.
     * Only available for post-matching filters.
     *
     * @return the resource info, or null if not set
     */
    public ResourceInfo getResourceInfo() {
        return resourceInfo;
    }

    /**
     * Set the ResourceInfo for the matched resource method.
     *
     * @param resourceInfo the resource info
     */
    public void setResourceInfo(ResourceInfo resourceInfo) {
        this.resourceInfo = resourceInfo;
        this.properties.put(RESOURCE_INFO_PROPERTY, resourceInfo);
    }

    @Override
    public Object getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public Collection<String> getPropertyNames() {
        return properties.keySet();
    }

    @Override
    public void setProperty(String name, Object object) {
        properties.put(name, object);
    }

    @Override
    public void removeProperty(String name) {
        properties.remove(name);
    }

    @Override
    public UriInfo getUriInfo() {
        return uriInfo;
    }

    @Override
    public void setRequestUri(URI requestUri) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRequestUri(URI baseUri, URI requestUri) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Request getRequest() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getMethod() {
        return request.prologue().method().text();
    }

    @Override
    public void setMethod(String method) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MultivaluedMap<String, String> getHeaders() {
        return httpHeaders.getRequestHeaders();
    }

    @Override
    public String getHeaderString(String name) {
        return httpHeaders.getHeaderString(name);
    }

    @Override
    public Date getDate() {
        return httpHeaders.getDate();
    }

    @Override
    public Locale getLanguage() {
        return httpHeaders.getLanguage();
    }

    @Override
    public int getLength() {
        return httpHeaders.getLength();
    }

    @Override
    public MediaType getMediaType() {
        return httpHeaders.getMediaType();
    }

    @Override
    public List<MediaType> getAcceptableMediaTypes() {
        return httpHeaders.getAcceptableMediaTypes();
    }

    @Override
    public List<Locale> getAcceptableLanguages() {
        return httpHeaders.getAcceptableLanguages();
    }

    @Override
    public Map<String, Cookie> getCookies() {
        return httpHeaders.getCookies();
    }

    @Override
    public boolean hasEntity() {
        return getLength() > 0;
    }

    @Override
    public InputStream getEntityStream() {
        return entityStream != null ? entityStream : request.content().inputStream();
    }

    @Override
    public void setEntityStream(InputStream input) {
        this.entityStream = input;
    }

    @Override
    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    @Override
    public void setSecurityContext(SecurityContext context) {
        this.securityContext = context;
    }

    @Override
    public void abortWith(Response response) {
        this.aborted = true;
        this.abortStatus = response.getStatus();
        this.abortMessage = response.getEntity() != null ? response.getEntity().toString() : "";
    }

    public boolean isAborted() {
        return aborted;
    }

    public int getAbortStatus() {
        return abortStatus;
    }

    public String getAbortMessage() {
        return abortMessage;
    }

    public ServerRequest getServerRequest() {
        return request;
    }
}


