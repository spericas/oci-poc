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

package io.helidon.examples.oci.poc;

import io.helidon.common.media.type.MediaTypes;
import io.helidon.http.HeaderNames;
import io.helidon.http.Status;
import io.helidon.webclient.http1.Http1Client;
import io.helidon.webserver.testing.junit5.ServerTest;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ServerTest
public class EchoEndpointTest {
    private final Http1Client client;

    public EchoEndpointTest(Http1Client client) {
        this.client = client;
    }

    @Test
    void testPing() {
        try (var response = client.get("/echo")
                .accept(MediaTypes.TEXT_PLAIN)
                .header(HeaderNames.AUTHORIZATION, "foo")
                .request()) {
            assertThat(response.status(), is(Status.OK_200));
            String entity = response.entity().as(String.class);
            assertThat(entity, is("pong"));
        }
    }

    @Test
    void testOnceSuccess() {
        var response = client.post("/echo/once")
                .accept(MediaTypes.TEXT_PLAIN)
                .contentType(MediaTypes.TEXT_PLAIN)
                .header(HeaderNames.AUTHORIZATION, "helidon")
                .submit("Hello World", String.class);

        assertThat(response.status(), is(Status.OK_200));
        String entity = response.entity();
        assertThat(entity, is("Hello World"));
    }

    @Test
    void testTwiceSuccess() {
        var response = client.post("/echo/twice")
                .accept(MediaTypes.TEXT_PLAIN)
                .contentType(MediaTypes.TEXT_PLAIN)
                .header(HeaderNames.AUTHORIZATION, "helidon")
                .submit("Hello World", String.class);

        assertThat(response.status(), is(Status.OK_200));
        String entity = response.entity();
        assertThat(entity, is("Hello WorldHello World"));
    }

    @Test
    void testOnceFailure() {
        var response = client.post("/echo/once")
                .accept(MediaTypes.TEXT_PLAIN)
                .contentType(MediaTypes.TEXT_PLAIN)
                .header(HeaderNames.AUTHORIZATION, "foo")
                .submit("Hello World", String.class);

        assertThat(response.status(), is(Status.UNAUTHORIZED_401));
    }

    @Test
    void testTwiceFailure() {
        var response = client.post("/echo/twice")
                .accept(MediaTypes.TEXT_PLAIN)
                .contentType(MediaTypes.TEXT_PLAIN)
                .header(HeaderNames.AUTHORIZATION, "foo")
                .submit("Hello World", String.class);

        assertThat(response.status(), is(Status.UNAUTHORIZED_401));
    }
}
