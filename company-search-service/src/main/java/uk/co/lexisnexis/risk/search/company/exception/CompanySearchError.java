package uk.co.lexisnexis.risk.search.company.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.co.lexisnexis.risk.search.company.constants.CompanySearchConstants;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanySearchError {

    private int status;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CompanySearchConstants.ERROR_DATE_FORMAT)
    private LocalDateTime timestamp;

}