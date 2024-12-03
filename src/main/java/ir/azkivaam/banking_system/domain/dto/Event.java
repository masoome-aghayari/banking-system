package ir.azkivaam.banking_system.domain.dto;

/*
 * @author masoome.aghayari
 * @since 12/1/24
 */

import ir.azkivaam.banking_system.domain.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Event<T> {
    private T data;
    private String message;
    private EventType eventType;
    private String[] arguments;

    public Event(T data, EventType eventType) {
        this.data = data;
        this.eventType = eventType;
    }
}
