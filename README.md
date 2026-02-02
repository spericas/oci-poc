## OCI POC

A proof of concept (POC) that simulates interactions between a Helidon SE Declarative application and a JAX-RS filter, similar to those used in Pegasus.

### Module helidon-examples-oci-poc-echo

Contains the sample Helidon SE Declarative application.

#### EchoEndpoint

A Helidon SE Declarative endpoint that exposes a few resource methods, some annotated 
with `@Authorized` --simulating an Identity SDK annotation.
Methods annotated with `@Authorized` are invoked only if the user is authorized. 
Upon successful authorization, user information must be available in the `User` 
request header.

#### AuthorizationFilter

A JAX-RS post-matching filter that simulates user authorization. It can inject `@Context`
instances that are available only at (or after) `@PostConstruct`.
The filter validates the user and, if successful, adds the `User` header to the 
request.

#### EchoEndpointTest

Verifies that authorization behaves correctly by sending two requests: one with valid 
credentials and one without.

### Module: helidon-examples-oci-poc-codegen

Annotation processor that generates a Helidon SE entry-point interceptor for each
method annotated with `@Authorized`. These interceptors are used to call the corresponding
authorization JAX-RS filters before the resource method is called.
If the JAX-RS filter chain does not abort processing, the interceptor proceeds with
invoking the resource method.

### Module: helidon-examples-oci-poc-jaxrs

Contains a set of classes that implement JAX-RS interfaces to support invoking JAX-RS
filters. It also includes a partial implementation of the JAX-RS `RuntimeDelegate`.
Many methods in this module are intentionally unimplemented at this stage.
