package ir.azkivaam.banking_system.observer.impl;

/*
 * @author masoome.aghayari
 * @since 12/3/24
 */

import ir.azkivaam.banking_system.domain.dto.BankAccountDto;
import ir.azkivaam.banking_system.domain.dto.Event;
import ir.azkivaam.banking_system.observer.BankAccountObserver;
import ir.azkivaam.banking_system.observer.ObserverManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BankAccountObserverManager implements ObserverManager<BankAccountObserver, BankAccountDto> {

    private static final List<BankAccountObserver> observers = new ArrayList<>();

    @Override
    public void subscribe(BankAccountObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(BankAccountObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Event<BankAccountDto> event) {
        observers.forEach(o -> o.update(event));
    }
}
