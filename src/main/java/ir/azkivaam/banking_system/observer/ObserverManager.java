package ir.azkivaam.banking_system.observer;

/*
 * @author masoome.aghayari
 * @since 12/3/24
 */

import ir.azkivaam.banking_system.domain.dto.Event;

public interface ObserverManager<T, F> {

    void subscribe(T observer);

    void unsubscribe(T observer);

    void notifyObservers(Event<F> event);

}
