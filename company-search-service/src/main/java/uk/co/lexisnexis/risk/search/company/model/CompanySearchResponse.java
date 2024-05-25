package uk.co.lexisnexis.risk.search.company.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

/**
 * company search response
 *
 * @author ravin
 * @date 2024/05/25
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CompanySearchResponse {

    private int totalResults;
    private List<Company> items;

}
