package uk.co.lexisnexis.risk.search.company.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import uk.co.lexisnexis.risk.search.company.validator.CompanySearchRequestValidator;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@CompanySearchRequestValidator()
public class CompanySearchRequest {

    private String companyName;
    private String companyNumber;

}
