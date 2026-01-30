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

package io.helidon.examples.oci.poc.codegen;

import io.helidon.common.types.TypeName;

final class OciTypes {

    static final TypeName AUTHORIZED_ANNOTATION = TypeName.create("io.helidon.examples.oci.poc.echo.Authorized");

    static final TypeName HTTP_ENTRYPOINT_INTERCEPTOR = TypeName.create("io.helidon.webserver.http.HttpEntryPoint.Interceptor");
    static final TypeName INTERCEPTOR_CONTEXT = TypeName.create("io.helidon.service.registry.InterceptionContext");
    static final TypeName CHAIN = TypeName.create("io.helidon.webserver.http.HttpEntryPoint.Interceptor.Chain");
    static final TypeName SERVER_REQUEST = TypeName.create("io.helidon.webserver.http.ServerRequest");
    static final TypeName SERVER_RESPONSE = TypeName.create("io.helidon.webserver.http.ServerResponse");

    private OciTypes() {
    }
}
