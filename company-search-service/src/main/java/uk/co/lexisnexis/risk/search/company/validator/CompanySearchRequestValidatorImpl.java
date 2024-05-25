package uk.co.lexisnexis.risk.search.company.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import uk.co.lexisnexis.risk.search.company.dto.CompanySearchRequest;

import java.util.Objects;

/**
 * company search request validator impl
 *
 * @author ravin
 * @date 2024/05/25
 */
public class CompanySearchRequestValidatorImpl implements ConstraintValidator<CompanySearchRequestValidator, CompanySearchRequest> {

  /**
   * is valid
   *
   * @param companySearchRequest       the company search request
   * @param constraintValidatorContext the constraint validator context
   * @return boolean is valid
   */
  @Override
  public boolean isValid(CompanySearchRequest companySearchRequest, ConstraintValidatorContext constraintValidatorContext) {
    return Objects.nonNull(
            companySearchRequest.getCompanyName()) || Objects.nonNull(companySearchRequest.getCompanyNumber());
  }
}