package uk.co.lexisnexis.risk.search.company.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    private String companyNumber;
    private String companyType;
    private String title;
    private String companyStatus;
    private String dateOfCreation;
    private Address address;
    private List<Officer> officers;

}
