package uk.co.lexisnexis.risk.search.company.exception;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CompanySearchCustomException extends Exception implements Serializable {

    private Throwable backendCause;
    private String additionalInfo;
    private String backendHttpStatus;
    private String backendCode;
    private String backendMessage;

}
