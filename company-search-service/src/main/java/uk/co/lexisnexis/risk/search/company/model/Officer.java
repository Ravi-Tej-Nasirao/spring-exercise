package uk.co.lexisnexis.risk.search.company.model;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Officer implements Serializable {
    private static final long serialVersionUID = 123L;

    private String name;
    private String officerRole;
    private String appointedOn;
    private String resignedOn;
    private Address address;

}
