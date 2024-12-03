package ir.azkivaam.banking_system.observer.impl;

/*
 * @author masoome.aghayari
 * @since 12/3/24
 */

import ir.azkivaam.banking_system.domain.dto.Event;
import ir.azkivaam.banking_system.domain.dto.TransactionDto;
import ir.azkivaam.banking_system.observer.ObserverManager;
import ir.azkivaam.banking_system.observer.TransactionObserver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionObserverManager implements ObserverManager<TransactionObserver, TransactionDto> {
    private static final List<TransactionObserver> observers = new ArrayList<>();


    @Override
    public void subscribe(TransactionObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(TransactionObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Event<TransactionDto> event) {
        observers.forEach(o -> o.update(event));
    }
}
