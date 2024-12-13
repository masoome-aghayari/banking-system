package ir.azkivaam.banking_system.exceptions;

/*
 * @author masoome.aghayari
 * @since 12/1/24
 */

import ir.azkivaam.banking_system.domain.enums.ErrorCode;
import lombok.Getter;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.MessageFormat;
import java.util.ResourceBundle;

@Getter
public class BankSystemException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String[] arguments;

    public BankSystemException(ErrorCode errorCode, String... arguments) {
        super();
        this.arguments = arguments;
        this.errorCode = errorCode;
    }

    public BankSystemException(ErrorCode errorCode, Exception cause, String... arguments) {
        super(cause);
        this.arguments = arguments;
        this.errorCode = errorCode;
    }

    public String getLocalizedMessage() {
        String messageTemplate = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale())
                                               .getString(errorCode.getValue());
        return arguments == null | arguments.length == 0 ?
               MessageFormat.format(messageTemplate, getCause().getLocalizedMessage()) :
               MessageFormat.format(messageTemplate, (Object[]) arguments);
    }
}
