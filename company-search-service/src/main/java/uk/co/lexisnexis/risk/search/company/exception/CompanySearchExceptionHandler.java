package uk.co.lexisnexis.risk.search.company.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * company search exception handler
 *
 * @author ravin
 * @date 2024/05/25
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class CompanySearchExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * handle method argument not valid
     *
     * @param ex      the ex
     * @param headers the headers
     * @param status  the status
     * @param request the request
     * @return ResponseEntity<Object> handle method argument not valid
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request)  {

        List<String> globalErrors = ex.getBindingResult().getGlobalErrors().stream().map(ObjectError :: getDefaultMessage).toList();
        List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream().map(FieldError :: getDefaultMessage).toList();

        List<String> errors = new ArrayList<>();
        errors.addAll(globalErrors);
        errors.addAll(fieldErrors);

        return new ResponseEntity<>(
                new CompanySearchError(HttpStatus.BAD_REQUEST.value(),
                    errors.stream().collect(Collectors.joining(",")),
                    LocalDateTime.now()),
                    status);
    }

    /**
     * handle company search custom exception
     *
     * @param ex the CompanySearchCustomException
     * @return ResponseEntity<Object> handle company search custom exception
     */
    @ExceptionHandler(CompanySearchCustomException.class)
    protected ResponseEntity<Object> handleCompanySearchCustomException(CompanySearchCustomException ex)  {

        return new ResponseEntity<>(
                new CompanySearchError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal server exception occurred. Please try after sometime.",
                        LocalDateTime.now()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
