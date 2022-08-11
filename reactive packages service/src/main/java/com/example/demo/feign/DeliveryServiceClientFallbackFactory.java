package com.example.demo.feign;

//import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import reactivefeign.FallbackFactory;

import java.util.function.Function;

@Component
public class DeliveryServiceClientFallbackFactory implements FallbackFactory<DeliveryServiceClient> { //FallbackFactory<DeliveryServiceClient> {

//    @Override
//    public DeliveryServiceClient create(Throwable cause) {
//        return new DeliveryServiceClientFallback(cause);
//    }

    @Override
    public DeliveryServiceClient apply(Throwable throwable) {
        return new DeliveryServiceClientFallback(throwable);
    }

    @Override
    public <V> Function<V, DeliveryServiceClient> compose(Function<? super V, ? extends Throwable> before) {
        return FallbackFactory.super.compose(before);
    }

    @Override
    public <V> Function<Throwable, V> andThen(Function<? super DeliveryServiceClient, ? extends V> after) {
        return FallbackFactory.super.andThen(after);
    }

}
