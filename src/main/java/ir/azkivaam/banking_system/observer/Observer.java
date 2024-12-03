package ir.azkivaam.banking_system.observer;

import ir.azkivaam.banking_system.domain.dto.Event;

public interface Observer<T> {
    void update(Event<T> event);
}

