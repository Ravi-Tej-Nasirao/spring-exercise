package uk.co.lexisnexis.risk.search.company.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import uk.co.lexisnexis.risk.search.company.model.CompanySearchResponse;

import java.time.Instant;
import java.util.Date;


@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Company {

    @Id
    @Indexed
    private String id;

    private uk.co.lexisnexis.risk.search.company.model.Company companyDetails;

    @Indexed(unique = true)
    private String companyNumber;

    @Indexed(unique = true)
    private String companyName;

    @CreatedDate
    private Instant createdDateTime;

    @LastModifiedDate
    private Instant modifiedDateTime;

    @Version
    private int version;
}
