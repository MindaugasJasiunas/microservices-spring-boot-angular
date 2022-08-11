package com.example.demo.feign;

//import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import reactivefeign.FallbackFactory;

import java.util.function.Function;

@Component
public class PaymentServiceClientFallbackFactory implements FallbackFactory<PaymentServiceClient> { //FallbackFactory<PaymentServiceClient> {

//    @Override
//    public PaymentServiceClient create(Throwable cause) {
//        return new PaymentServiceClientFallback(cause);
//    }

    @Override
    public PaymentServiceClient apply(Throwable throwable) {
        return new PaymentServiceClientFallback(throwable);
    }

    @Override
    public <V> Function<V, PaymentServiceClient> compose(Function<? super V, ? extends Throwable> before) {
        return FallbackFactory.super.compose(before);
    }

    @Override
    public <V> Function<Throwable, V> andThen(Function<? super PaymentServiceClient, ? extends V> after) {
        return FallbackFactory.super.andThen(after);
    }
}
