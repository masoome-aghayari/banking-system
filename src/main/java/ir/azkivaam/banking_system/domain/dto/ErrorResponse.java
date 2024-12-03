package ir.azkivaam.banking_system.domain.dto;

/*
 * @author masoome.aghayari
 * @since 12/1/24
 */

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ErrorResponse<T> {
    private T data;
    private Date date;
    private String path;
    private String message;
    private String localizedMessage;

    public ErrorResponse(String message, String localizedMessage, T data, Date date, String path) {
        this.message = message;
        this.localizedMessage = localizedMessage;
        this.data = data;
        this.date = date;
        this.path = path;
    }

    public ErrorResponse(String message, String localizedMessage, Date date, String path) {
        this.message = message;
        this.localizedMessage = localizedMessage;
        this.date = date;
        this.path = path;
    }
}
