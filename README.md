## OCI POC

A proof of concept (POC) that simulates interactions between a Helidon SE Declarative application and a JAX-RS filter, similar to those used in Pegasus.

### Module helidon-examples-oci-poc-echo

Contains the sample Helidon SE Declarative application.


#### EchoEndpoint

A Helidon SE Declarative endpoint that exposes a single echo resource method annotated 
with `@Authorized` --simulating an Identity SDK annotation.
The resource method is invoked only if the user is authorized. Upon successful 
authorization, user information must be available in the `User` request header.

#### AuthorizationFilter

A JAX-RS post-matching filter that simulates user authorization. It can inject `@Context`
instances that are available only at (or after) `@PostConstruct`.
The filter validates the user and, if successful, adds the `User` header to the 
request.

#### AuthorizationInterceptor

A Helidon SE entry-point interceptor that executes when the echo resource method is 
invoked. It is aware of the `@Authorized` annotation and is responsible for instantiating, 
injecting, and invoking the `AuthorizationFilter`.
If the filter does not abort processing, the interceptor proceeds with invoking the 
echo method.

TODO: This interceptor should be code-generated using an annotation processor.

#### OciPocTest

Verifies that authorization behaves correctly by sending two requests: one with valid 
credentials and one without.

### Module: helidon-examples-oci-poc-jaxrs

Contains a set of classes that implement JAX-RS interfaces to support invoking JAX-RS
filters. It also includes a partial implementation of the JAX-RS `RuntimeDelegate`.
Many methods in this module are intentionally unimplemented at this stage.
