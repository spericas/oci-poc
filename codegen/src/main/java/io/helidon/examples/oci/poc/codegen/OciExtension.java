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

import io.helidon.codegen.classmodel.ClassModel;
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

    OciExtension(RegistryCodegenContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void process(RegistryRoundContext roundContext) {
        for (TypeInfo typeInfo : roundContext.types()) {
            int index = 0;
            for (TypedElementInfo elementInfo : typeInfo.elementInfo()) {
                if (elementInfo.hasAnnotation(OciTypes.AUTHORIZED_ANNOTATION)) {
                    var generatedType = generatedTypeName(typeInfo.typeName(),
                                                          elementInfo,
                                                          OciTypes.AUTHORIZED_ANNOTATION,
                                                          index++);
                    generateInterceptor(roundContext, generatedType, typeInfo, elementInfo);
                }
            }
        }
    }

    private void generateInterceptor(RegistryRoundContext roundContext,
                                     TypeName generatedType,
                                     TypeInfo typeInfo,
                                     TypedElementInfo elementInfo) {
        ClassModel.Builder builder = ClassModel.builder()
                .accessModifier(AccessModifier.PACKAGE_PRIVATE)
                .addAnnotation(Annotation.create(ServiceCodegenTypes.SERVICE_ANNOTATION_SINGLETON))
                .type(generatedType)
                .addInterface(OciTypes.HTTP_ENTRYPOINT_INTERCEPTOR)
                .sortStaticFields(false);

        builder.addImport(TypeNames.TYPE_NAME)
                .addImport(TypeNames.TYPED_ELEMENT_INFO)
                .addImport("io.helidon.examples.oci.poc.echo.AuthorizationFilter")
                .addImport("io.helidon.examples.oci.poc.jaxrs.HelidonContainerRequestContext")
                .addImport("io.helidon.examples.oci.poc.jaxrs.HelidonContextInjector");

        builder.addField(field -> field.name("LOGGER")
                .isStatic(true)
                .isFinal(true)
                .accessModifier(AccessModifier.PRIVATE)
                .type("System.Logger")
                .addContent("System.getLogger(\"" + generatedType.name() + "\")"));

        builder.addField(field -> field.name("TYPE_NAME")
                .isStatic(true)
                .isFinal(true)
                .accessModifier(AccessModifier.PRIVATE)
                .type(TypeNames.TYPE_NAME)
                .addContent("TypeName.create(\"" + typeInfo.typeName() + "\")"));

        builder.addField(field -> field.name("ELEMENT_SIGNATURE")
                .isStatic(true)
                .isFinal(true)
                .accessModifier(AccessModifier.PRIVATE)
                .type(TypeNames.STRING)
                .addContent("\"" + elementInfo.signature() + "\""));

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
                                        
                                        if (interceptionContext.serviceInfo().serviceType().equals(TYPE_NAME) &&
                                                typedElementInfo.signature().toString().equals(ELEMENT_SIGNATURE)) {
                                            LOGGER.log(System.Logger.Level.DEBUG, "Intercepting call '" +  ELEMENT_SIGNATURE + "'");
                                        
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

    private TypeName generatedTypeName(TypeName typeName,
                                       TypedElementInfo element,
                                       TypeName annotation,
                                       int index) {
        return TypeName.builder()
                .packageName(typeName.packageName())
                .className(typeName.classNameWithEnclosingNames().replace('.', '_') + "_"
                                   + element.elementName()
                                   + "_" + index
                                   + "_" + annotation.className())
                .build();

    }
}
