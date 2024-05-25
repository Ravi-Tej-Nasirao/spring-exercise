package uk.co.lexisnexis.risk.search.company.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OfficerResponse {

    public String etag;
    public String kind;
    public int itemsPerPage;
    public ArrayList<OfficerItem> items;
    public int activeCount;
    public int totalResults;
    public int resignedCount;

}
