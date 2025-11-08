package com.paul.shitment.shipment_service.services.impl;

import java.util.function.Predicate;

import org.springframework.stereotype.Component;

@Component
public class ServiceHelper {

    private <T> boolean setActivePredicateValue(T entity, Predicate<T> isActive) {
        return !isActive.test(entity);
    }
    
    public <T> void updateStatusIfChanged(T entity, Predicate<T> isActive,
            java.util.function.Consumer<T> setActive) {
        if (isActive.test(entity) != setActivePredicateValue(entity, isActive)) {
            setActive.accept(entity);
        }
    }

}
