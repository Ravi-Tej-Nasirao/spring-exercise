package uk.co.lexisnexis.risk.search.company.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanySearchRequest {

    private String companyName;
    private String companyNumber;

}
