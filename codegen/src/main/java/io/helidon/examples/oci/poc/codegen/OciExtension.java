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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.helidon.codegen.classmodel.ClassModel;
import io.helidon.codegen.classmodel.Field;
import io.helidon.common.types.AccessModifier;
import io.helidon.common.types.Annotation;
import io.helidon.common.types.Annotations;
import io.helidon.common.types.TypeInfo;
import io.helidon.common.types.TypeName;
import io.helidon.common.types.TypeNames;
import io.helidon.common.types.TypedElementInfo;
import io.helidon.service.codegen.RegistryCodegenContext;
import io.helidon.service.codegen.RegistryRoundContext;
import io.helidon.service.codegen.ServiceCodegenTypes;
import io.helidon.service.codegen.spi.RegistryCodegenExtension;

class OciExtension implements RegistryCodegenExtension {

    private final RegistryCodegenContext ctx;
    private final Map<String, Set<String>> methods = new HashMap<>();

    OciExtension(RegistryCodegenContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void process(RegistryRoundContext roundContext) {
        // init processing
        methods.clear();

        // collect all methods and group them by package
        for (TypeInfo typeInfo : roundContext.types()) {
            for (TypedElementInfo elementInfo : typeInfo.elementInfo()) {
                if (elementInfo.hasAnnotation(OciTypes.AUTHORIZED_ANNOTATION)) {
                    String packageName = typeInfo.typeName().packageName();
                    Set<String> set = methods.computeIfAbsent(packageName, k -> new HashSet<>());
                    set.add(typeInfo.typeName().toString() + "::" + elementInfo.signature().toString());
                }
            }
        }

        // generate an interceptor per package
        for (String packageName : methods.keySet()) {
            Set<String> methodElements = methods.get(packageName);
            TypeName generatedType = TypeName.builder()
                    .packageName(packageName)
                    .className("Authorized_interceptor")
                    .build();
            generateInterceptor(roundContext, generatedType, methodElements);
        }
    }

    private void generateInterceptor(RegistryRoundContext roundContext,
                                     TypeName generatedType,
                                     Set<String> methodElements) {
        ClassModel.Builder builder = ClassModel.builder()
                .accessModifier(AccessModifier.PACKAGE_PRIVATE)
                .addAnnotation(Annotation.create(ServiceCodegenTypes.SERVICE_ANNOTATION_SINGLETON))
                .type(generatedType)
                .addInterface(OciTypes.HTTP_ENTRYPOINT_INTERCEPTOR)
                .sortStaticFields(false);

        builder.addImport(TypeNames.SET)
                .addImport(TypeNames.TYPE_NAME)
                .addImport(TypeNames.TYPED_ELEMENT_INFO)
                .addImport("java.util.HashSet")
                .addImport("io.helidon.examples.oci.poc.echo.AuthorizationFilter")
                .addImport("io.helidon.examples.oci.poc.jaxrs.HelidonContainerRequestContext")
                .addImport("io.helidon.examples.oci.poc.jaxrs.HelidonContextInjector");

        builder.addField(field -> field.name("LOGGER")
                .isStatic(true)
                .isFinal(true)
                .accessModifier(AccessModifier.PRIVATE)
                .type("System.Logger")
                .addContent("System.getLogger(\"" + generatedType.name() + "\")"));

        Field.Builder fieldBuilder = Field.builder();
        fieldBuilder.name("INTERCEPTED_METHODS")
                .isStatic(true)
                .isFinal(true)
                .accessModifier(AccessModifier.PRIVATE)
                .type("Set<String>")
                .addContent("new HashSet(Set.of(\n")
                .increaseContentPadding();
        boolean first = true;
        for (String methodElement : methodElements) {
            if (!first) {
                fieldBuilder.addContent(",\n");
            }
            first = false;
            fieldBuilder.addContent("\"" + methodElement + "\"");
        }
        fieldBuilder.addContent("))");
        builder.addField(fieldBuilder.build());

        builder.addMethod(proceed -> proceed.addAnnotation(Annotations.OVERRIDE)
                .returnType(TypeNames.PRIMITIVE_VOID)
                .accessModifier(AccessModifier.PUBLIC)
                .name("proceed")
                .addParameter(p -> p.type(OciTypes.INTERCEPTOR_CONTEXT)
                        .name("interceptionContext"))
                .addParameter(p -> p.type(OciTypes.CHAIN)
                        .name("chain"))
                .addParameter(p -> p.type(OciTypes.SERVER_REQUEST)
                        .name("request"))
                .addParameter(p -> p.type(OciTypes.SERVER_RESPONSE)
                        .name("response"))
                .addThrows(t -> t.type(Exception.class))
                .addContentLine("""
                                        TypedElementInfo typedElementInfo = interceptionContext.elementInfo();
                                         String method = interceptionContext.serviceInfo().serviceType().toString() + "::" + typedElementInfo.signature();
                                        
                                         if (INTERCEPTED_METHODS.contains(method)) {
                                             LOGGER.log(System.Logger.Level.DEBUG, "Intercepting call '" + typedElementInfo.signature() + "'");
                                        
                                             AuthorizationFilter filter = new AuthorizationFilter();
                                             HelidonContainerRequestContext context = new HelidonContainerRequestContext(request);
                                             HelidonContextInjector.inject(filter, context);
                                             HelidonContextInjector.postConstruct(filter, context);
                                        
                                             filter.filter(context);
                                        
                                             if (context.isAborted()) {
                                                String msg = context.getAbortMessage();
                                                response.status(context.getAbortStatus()).send(msg != null ? msg : "");
                                                return;
                                             }
                                         }
                            
                                         chain.proceed(request, response);"""));

        roundContext.addGeneratedType(generatedType,
                                      builder,
                                      generatedType);
    }
}
