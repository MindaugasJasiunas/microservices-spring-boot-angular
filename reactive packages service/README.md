# Packages service

Packages service is the main service that handles parcels. It has orchestration type SAGA design pattern & uses Axon framework for CQRS.

- Spring WebFlux (Project Reactor)
- Reactive MongoDB
- Axon framework (CQRS) + reactive support + Orchestration SAGA pattern
- Reactive Feign Client
- Global error handler with custom Error Attributes
- Object (package) validator using Combinator pattern
- Functional endpoints using Router/Handler

## TODO
- Implement Email microservice
- Implement Delivery microservice instead of mocked.
- Implement Payment microservice instead of mocked.
- Add additional SAGA events & commands like `ParcelInboundEvent`, `SendDeliveryConfirmationEmailCommand` and much more.
- Add Reactive Spring Security
- Add JSON Web Token (JWT) for authentication (custom Authentication Manager & Security Context Repository) - stateless application.
- Add CORS filter
- Add tests and lots of them !
  
__(!) Not sure if Axon framework was the right choice even with all the `ReactorCommandGateway` and `ReactorQueryGateway`. We are using reactive repositories, because of that we must _subscribe_ in application's Query side (otherwise data is not saved in read DB - backpressure needed).__


## Axon framework workarounds

### Axon Reactive Support
For Axon framework to support reactive model, we need to add additional dependency.
It changes `CommandGateway` to `ReactorCommandGateway`, `QueryGateway` to `ReactorQueryGateway`, `EventGateway` to `ReactorEventGateway` and add many more additional features.

### Reactive Axon Query Handlers
Reactive Axon model does not yet support returning of `Mono` or `Flux` from `@QueryHandler`'s. We need to make sure to return `CompletableFuture` & return everything with `.toFuture()` instead.

### Axon serialization/deserialization 
#### Serialization / deserialization workaroud
To prevent error `com.thoughtworks.xstream.converters.ConversionException: <...>`
we need to override Axon Serializer in [application properties](https://github.com/MindaugasJasiunas/microservices-spring-boot-angular/blob/main/reactive%20packages%20service/src/main/resources/application.yml) to use Jackson.
```
axon:
  serializer:
    general: jackson
    events: jackson
    messages: Jackson
```

#### Page, Pageable & PageImpl
Axon does not support these types.

- To use Pageable and return Mono with Page of objects(in this case - Package objects) we need to implement custom `PageImpl` class and use it as return for query handler and service ([`CustomPageImpl`](https://github.com/MindaugasJasiunas/microservices-spring-boot-angular/blob/main/reactive%20packages%20service/src/main/java/com/example/demo/util/CustomPageImpl.java)).
- To return Page of objects we also need to create custom _Response Type_ ([`PageResponseType`](https://github.com/MindaugasJasiunas/microservices-spring-boot-angular/blob/main/reactive%20packages%20service/src/main/java/com/example/demo/util/PageResponseType.java)).
- For query class (query record in our case) in query handler to have PageRequest as argument and not throw `Cannot construct instance of 'org.springframework.data.domain.Sort' problem: You have to provide at least one property to sort by` exception - we need to implement custom PageRequest deserializer & register it as Module @Bean ([`PageRequestDeserializer`](https://github.com/MindaugasJasiunas/microservices-spring-boot-angular/blob/main/reactive%20packages%20service/src/main/java/com/example/demo/util/json/PageRequestDeserializer.java)).

## AXON Saga

- Send new Package as JSON payload

```
[POST] http://localhost:<packagesServicePort>/parcels/submitNewPackage
```
CreateParcelCommand -> Aggregate(ParcelCreatedEvent) -> Saga(ProcessPaymentCommand) -> Aggregate(PaymentPendingEvent)

- (Payment Service) Approve or decline payment for package

```
[GET] http://localhost:<paymentServicePort>/payment/approve/<packagePublicId>
```
PaymentApprovalCommand -> Aggregate(PaymentApprovedEvent) -> Saga(ReservePickupCommand) -> Aggregate(PickupConfirmationPendingEvent)
```
[GET] http://localhost:<paymentServicePort>/payment/decline/<packagePublicId>
```
PaymentRejectionCommand -> Aggregate(PaymentRejectedEvent) -> Saga(DisableParcelCommand) -> Aggregate(ParcelDisabledEvent)

- (Delivery Service) Confirm Pickup date & time for package
```
[GET] http://localhost:<deliveryServicePort>/assign/<packagePublicId>
```
PickupAssignedCommand -> Aggregate(PickupPendingEvent)

- (Delivery Service) Pickup package
```
[GET] http://localhost:<deliveryServicePort>/picked-up/<packagePublicId>
```
InTransitCommand -> Aggregate(InTransitEvent)

- (Delivery Service) Deliver or fail delivering package
```
[GET] http://localhost:<deliveryServicePort>/delivered/<packagePublicId>
```
DeliveredCommand -> Aggregate(ParcelFinishedEvent) -> @EndSaga
```
[GET] http://localhost:<deliveryServicePort>/delivery-error/<packagePublicId>
```
InTransitCommand -> Aggregate(DeliveryErrorEvent) -> Saga(ReturnToSenderCommand) -> Aggregate(PickupConfirmationPendingEvent)

(!) ParcelsProjection(Event Handler) DeliveryErrorEvent - sender and receiver change places & marks package as 'return' (if not already returning package).



