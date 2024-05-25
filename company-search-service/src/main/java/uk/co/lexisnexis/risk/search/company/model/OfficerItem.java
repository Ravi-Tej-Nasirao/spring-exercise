package uk.co.lexisnexis.risk.search.company.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OfficerItem {
    public Address address;
    public String name;
    public String appointedOn;
    public String officerRole;
    public String occupation;
    public String nationality;
    public DateOfBirth dateOfBirth;
    public String countryOfResidence;
    public String resignedOn;
}