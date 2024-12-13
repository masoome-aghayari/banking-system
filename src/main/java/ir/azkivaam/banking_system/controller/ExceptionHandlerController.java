package ir.azkivaam.banking_system.controller;

import ir.azkivaam.banking_system.config.LocaleConfig;
import ir.azkivaam.banking_system.domain.dto.ErrorResponse;
import ir.azkivaam.banking_system.domain.enums.ErrorCode;
import ir.azkivaam.banking_system.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author masoome.aghayari
 * @since 12/1/24
 */

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerController {
    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                                                         WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
          .getFieldErrors()
          .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        String localizedMessage = messageSource.getMessage(ErrorCode.VALIDATION.getValue(),
                                                           errors.values().toArray(), LocaleConfig.LOCALE);
        String message = errors.entrySet()
                               .stream()
                               .map(entry -> entry.getKey() + " = " + entry.getValue())
                               .collect(Collectors.joining(", ", "{", "}"));
        return getErrorResponseWithData(message,
                                        localizedMessage,
                                        HttpStatus.BAD_REQUEST,
                                        request.getContextPath(),
                                        errors);
    }

    @ExceptionHandler({BankSystemException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse<Object>> handleBankServiceException(BankSystemException ex,
                                                                            WebRequest request) {
        String localizedMessage = ex.getLocalizedMessage();
        return getErrorResponse(ex.getMessage(),
                                localizedMessage,
                                request.getContextPath(),
                                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TransactionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse<Object>> handleTransactionException(TransactionException ex,
                                                                            WebRequest request) {
        String localizedMessage = ex.getLocalizedMessage();
        return getErrorResponse(ex.getMessage(),
                                localizedMessage,
                                request.getContextPath(),
                                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BankAccountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse<Object>> handleBankAccountException(BankSystemException ex,
                                                                            WebRequest request) {
        String localizedMessage = ex.getLocalizedMessage();
        return getErrorResponse(ex.getMessage(),
                                localizedMessage,
                                request.getContextPath(),
                                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountCreationException.class)
    public ResponseEntity<ErrorResponse<Object>> handleAccountCreationException(AccountCreationException ex,
                                                                                WebRequest request) {
        String localizedMessage = ex.getLocalizedMessage();
        return getErrorResponse(ex.getMessage(), localizedMessage, request.getContextPath(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse<Object>> handleHandlerMethodValidationException(HandlerMethodValidationException ex,
                                                                                        WebRequest request) {
        String localizedMessage = ex.getLocalizedMessage();
        return getErrorResponse(ex.getMessage(), localizedMessage, request.getContextPath(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BranchNotFoundException.class)
    public ResponseEntity<ErrorResponse<Object>> handleBranchNotFoundException(BranchNotFoundException ex,
                                                                               WebRequest request) {
        String localizedMessage =
                messageSource.getMessage(ex.getErrorCode().getValue(), ex.getArguments(), LocaleConfig.LOCALE);
        return getErrorResponse(ex.getMessage(), localizedMessage, request.getContextPath(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse<Object>> handleGeneralException(Exception ex, WebRequest request) {
        String localizedMessage = messageSource.getMessage(ErrorCode.GENERAL.getValue(), null, LocaleConfig.LOCALE);
        return getErrorResponse(ex.getMessage(),
                                localizedMessage,
                                request.getContextPath(),
                                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse<Object>> handleTypeMisMatchException(TypeMismatchException ex,
                                                                             WebRequest request) {
        String localizedMessage =
                messageSource.getMessage(ErrorCode.TYPE_MISMATCH.getValue(), null, LocaleConfig.LOCALE);
        return getErrorResponse(ex.getMessage(), localizedMessage, request.getContextPath(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse<Object>> handleAuthenticationException(AuthenticationException ex,
                                                                               WebRequest request) {
        String localizedMessage =
                messageSource.getMessage(ErrorCode.UN_AUTHORIZED.getValue(), null, LocaleConfig.LOCALE);
        return getErrorResponse(ex.getMessage(), localizedMessage, request.getContextPath(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse<Object>> handleAccessDeniedException(AccessDeniedException ex,
                                                                             WebRequest request) {
        String localizedMessage = messageSource.getMessage(ErrorCode.FORBIDDEN.getValue(), null, LocaleConfig.LOCALE);
        return getErrorResponse(ex.getMessage(), localizedMessage, request.getContextPath(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse<Object>> resourceNotFoundExceptionHandler(NoResourceFoundException ex,
                                                                                  WebRequest request) {
        String localizedMessage = messageSource.getMessage(ErrorCode.NOT_FOUND.getValue(), null, LocaleConfig.LOCALE);
        return getErrorResponse(ex.getMessage(), localizedMessage, request.getContextPath(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodNotAllowedException.class, HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<ErrorResponse<Object>> methodNotAllowedExceptionHandler(Exception ex, WebRequest request) {
        String localizedMessage =
                messageSource.getMessage(ErrorCode.METHOD_NOT_ALLOWED.getValue(), null, LocaleConfig.LOCALE);
        return getErrorResponse(ex.getMessage(),
                                localizedMessage,
                                request.getContextPath(),
                                HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ResponseEntity<ErrorResponse<MediaType>> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                                    WebRequest request) {
        String localizedMessage =
                messageSource.getMessage(ErrorCode.MEDIA_NOT_SUPPORTED.getValue(), null, LocaleConfig.LOCALE);
        return getErrorResponseWithData(ex.getMessage(),
                                        localizedMessage,
                                        HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                                        request.getContextPath(),
                                        ex.getContentType());
    }

    public <T> ResponseEntity<ErrorResponse<T>> getErrorResponseWithData(String message,
                                                                         String localizedMessage,
                                                                         HttpStatus status,
                                                                         String path,
                                                                         T data) {
        ErrorResponse<T> errorResponse = new ErrorResponse<>(message, localizedMessage, data, new Date(), path);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    protected ResponseEntity<ErrorResponse<Object>> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                                                                     WebRequest request) {
        String localizedMessage = messageSource.getMessage("error.wrong.media.type", null, LocaleConfig.LOCALE);
        return getErrorResponse(ex.getMessage(), localizedMessage, request.getContextPath(), HttpStatus.NOT_ACCEPTABLE);
    }

    private ResponseEntity<ErrorResponse<Object>> getErrorResponse(String message,
                                                                   String localizedMessage,
                                                                   String contextPath,
                                                                   HttpStatus httpStatus) {
        ErrorResponse<Object> errorResponse = new ErrorResponse<>(message, localizedMessage, new Date(), contextPath);
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
