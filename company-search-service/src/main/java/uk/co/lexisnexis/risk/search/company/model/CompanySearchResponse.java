package uk.co.lexisnexis.risk.search.company.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * company search response
 *
 * @author ravin
 * @date 2024/05/25
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanySearchResponse implements Serializable {
    private static final long serialVersionUID = 123L;

    private int totalResults;
    private List<Company> items;

}
