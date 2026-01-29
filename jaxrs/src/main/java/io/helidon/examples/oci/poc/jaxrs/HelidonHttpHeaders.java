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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.helidon.webserver.http.ServerRequest;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;

/**
 * Wrapper that adapts Helidon's ServerRequest headers to JAX-RS HttpHeaders.
 */
public class HelidonHttpHeaders implements HttpHeaders {

    private final MultivaluedMap<String, String> headers;

    public HelidonHttpHeaders(ServerRequest request) {
        this.headers = new HelidonMultivaluedHashMap(request);
    }

    @Override
    public List<String> getRequestHeader(String name) {
        return headers.get(name);
    }

    @Override
    public String getHeaderString(String name) {
        return headers.getFirst(name);
    }

    @Override
    public MultivaluedMap<String, String> getRequestHeaders() {
        return headers;
    }

    @Override
    public List<MediaType> getAcceptableMediaTypes() {
        String accept = getHeaderString("Accept");
        if (accept == null || accept.isEmpty()) {
            return List.of(MediaType.WILDCARD_TYPE);
        }
        List<MediaType> types = new ArrayList<>();
        for (String part : accept.split(",")) {
            try {
                types.add(MediaType.valueOf(part.trim()));
            } catch (IllegalArgumentException e) {
                // Skip invalid media types
            }
        }
        return types.isEmpty() ? List.of(MediaType.WILDCARD_TYPE) : types;
    }

    @Override
    public List<Locale> getAcceptableLanguages() {
        String acceptLang = getHeaderString("Accept-Language");
        if (acceptLang == null || acceptLang.isEmpty()) {
            return List.of(Locale.getDefault());
        }
        List<Locale> locales = new ArrayList<>();
        for (String part : acceptLang.split(",")) {
            String lang = part.split(";")[0].trim();
            if (!lang.isEmpty()) {
                locales.add(Locale.forLanguageTag(lang));
            }
        }
        return locales.isEmpty() ? List.of(Locale.getDefault()) : locales;
    }

    @Override
    public MediaType getMediaType() {
        String contentType = getHeaderString("Content-Type");
        return contentType != null ? MediaType.valueOf(contentType) : null;
    }

    @Override
    public Locale getLanguage() {
        String lang = getHeaderString("Content-Language");
        return lang != null ? Locale.forLanguageTag(lang) : null;
    }

    @Override
    public Map<String, Cookie> getCookies() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public Date getDate() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public int getLength() {
        String length = getHeaderString("Content-Length");
        if (length != null) {
            try {
                return Integer.parseInt(length);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }
}


