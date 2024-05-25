package uk.co.lexisnexis.risk.search.company.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.lexisnexis.risk.search.company.constants.CompanySearchConstants;
import uk.co.lexisnexis.risk.search.company.dto.CompanySearchRequest;
import uk.co.lexisnexis.risk.search.company.dto.CompanySearchResponse;
import uk.co.lexisnexis.risk.search.company.exception.CompanySearchCustomException;
import uk.co.lexisnexis.risk.search.company.exception.CompanySearchError;
import uk.co.lexisnexis.risk.search.company.service.CompanySearchCacheService;
import uk.co.lexisnexis.risk.search.company.service.CompanySearchService;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * company search controller
 *
 * @author ravin
 * @date 2024/05/25
 */
@Slf4j
@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanySearchController {

    @Autowired
    private CompanySearchService companySearchService;

    @Autowired
    private CompanySearchCacheService companySearchCacheService;

    /**
     * search companies
     *
     * @param apiKey               the api key
     * @param isActiveCompanyOnly  the is active company only
     * @param isActiveOfficersOnly the is active officers only
     * @param isLatestDataRequired the is latest data required
     * @param companySearchRequest the company search request
     * @return ResponseEntity<Object> search companies
     * @throws CompanySearchCustomException CompanySearchCustomException
     */
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {CompanySearchConstants.APPLICATION_JSON_VERSION_1, CompanySearchConstants.APPLICATION_XML_VERSION_1}
    )
    public ResponseEntity<Object> searchCompanies(
            @RequestHeader(CompanySearchConstants.API_KEY) String apiKey,
            @RequestParam(defaultValue = CompanySearchConstants.TRUE) boolean isActiveCompanyOnly,
            @RequestParam(defaultValue = CompanySearchConstants.TRUE) boolean isActiveOfficersOnly,
            @RequestParam(defaultValue = CompanySearchConstants.FALSE) boolean isLatestDataRequired,
            @Valid @RequestBody CompanySearchRequest companySearchRequest
            ) throws CompanySearchCustomException {

        log.info("Fetching the details for Company Name : {}, Company Number : {} ",
                companySearchRequest.getCompanyName(), companySearchRequest.getCompanyNumber());

        Optional<CompanySearchResponse> companySearchResponse;
        if(isLatestDataRequired){
            companySearchResponse = companySearchService.searchCompanies(apiKey, companySearchRequest, isActiveCompanyOnly, isActiveOfficersOnly, isLatestDataRequired);
        } else {
            companySearchResponse = companySearchCacheService.searchCompanies(apiKey, companySearchRequest, isActiveCompanyOnly, isActiveOfficersOnly, isLatestDataRequired);
        }

        if(companySearchResponse.isPresent()){
            return new ResponseEntity<>(companySearchResponse.get(), HttpStatus.OK);
        }

        CompanySearchError companyNotFoundResponse = new CompanySearchError();
        companyNotFoundResponse.setMessage(
                MessageFormatter.format("Company Details are not available for Company Name : {}, Company Number : {} ",
                companySearchRequest.getCompanyName(), companySearchRequest.getCompanyNumber()).getMessage());
        companyNotFoundResponse.setStatus(HttpStatus.NOT_FOUND.value());
        companyNotFoundResponse.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(companyNotFoundResponse, HttpStatus.NOT_FOUND);

    }

}
