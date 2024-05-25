package uk.co.lexisnexis.risk.search.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Company implements Serializable {
    private static final long serialVersionUID = 123L;

    private String companyNumber;
    private String companyType;
    private String title;
    private String companyStatus;
    private String dateOfCreation;
    private Address address;
    private List<Officer> officers;

}
