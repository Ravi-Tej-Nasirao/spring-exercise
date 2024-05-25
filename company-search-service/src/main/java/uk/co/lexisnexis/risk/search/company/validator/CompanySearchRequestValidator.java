package uk.co.lexisnexis.risk.search.company.validator;

import jakarta.validation.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * company search request validator
 *
 * @author ravin
 * @date 2024/05/25
 */
@Documented
@Constraint(validatedBy = CompanySearchRequestValidatorImpl.class)
@Target({TYPE, FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface CompanySearchRequestValidator {

  String message() default "Either Company Name or Number must be provided.";

  Class[] groups() default {};

  Class[] payload() default {};
}