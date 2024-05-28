package uk.co.lexisnexis.risk.search.company.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import uk.co.lexisnexis.risk.search.company.exception.CompanySearchCustomException;
import uk.co.lexisnexis.risk.search.company.helper.ApiService;
import uk.co.lexisnexis.risk.search.company.mapper.CompanySearchMapper;
import uk.co.lexisnexis.risk.search.company.model.CompanySearchRequest;
import uk.co.lexisnexis.risk.search.company.model.CompanySearchResponse;
import uk.co.lexisnexis.risk.search.company.model.Officer;
import uk.co.lexisnexis.risk.search.company.model.OfficerResponse;

import java.util.*;
import java.util.stream.Collectors;

import static uk.co.lexisnexis.risk.search.company.constants.CompanySearchConstants.*;

/**
 * tru proxy service
 *
 * @author ravin
 * @date 2024/05/25
 */
@Slf4j
@Service
public class TruProxyService {

    @Value("${search.company}")
    private String searchCompanyBaseUrl;

    @Autowired
    private ApiService apiService;

    @Autowired
    private CompanySearchMapper companySearchMapper;

    /**
     * search companies
     *
     * @param apiKey               the api key
     * @param companySearchRequest the company search request
     * @param isActiveCompanyOnly  the is active company only
     * @param isActiveOfficersOnly the is active officers only
     * @return Optional<CompanySearchResponse> search companies
     * @throws CompanySearchCustomException CompanySearchCustomException
     */
    public Optional<uk.co.lexisnexis.risk.search.company.model.CompanySearchResponse> searchCompanies(String apiKey,
                                                                                                    uk.co.lexisnexis.risk.search.company.dto.CompanySearchRequest companySearchRequest,
                                                                                                    boolean isActiveCompanyOnly, boolean isActiveOfficersOnly) throws CompanySearchCustomException {

        CompanySearchRequest companySearchRequestModel = companySearchMapper.mapCompanySearchRequestDtoToModel(companySearchRequest);

        CompanySearchResponse companySearchResponse = getCompanyDetails(apiKey, companySearchRequestModel, isActiveCompanyOnly);
        companySearchResponse = populateOfficerDetails(apiKey, companySearchResponse, isActiveCompanyOnly, isActiveOfficersOnly);

        return Objects.nonNull(companySearchResponse) ? Optional.ofNullable(companySearchResponse) : Optional.empty();
    }

    /**
     * populate officer details
     *
     * @param apiKey                the api key
     * @param companySearchResponse the company search response
     * @param isActiveCompanyOnly   the is active company only
     * @param isActiveOfficersOnly  the is active officers only
     * @return CompanySearchResponse populate officer details
     * @throws CompanySearchCustomException CompanySearchCustomException
     */
    private CompanySearchResponse populateOfficerDetails(String apiKey, CompanySearchResponse companySearchResponse,
                                                         boolean isActiveCompanyOnly, boolean isActiveOfficersOnly) throws CompanySearchCustomException{

        if(Objects.isNull(companySearchResponse) || CollectionUtils.isEmpty(companySearchResponse.getItems())){
            return companySearchResponse;
        }
        try{
            companySearchResponse.getItems()
                    .forEach(company-> {
                        company.setOfficers(new ArrayList<>(getOfficerDetails(apiKey, company.getCompanyNumber(), isActiveOfficersOnly)));
                    });
        } catch (Exception e) {
            throw CompanySearchCustomException.builder().backendMessage(e.getMessage()).build();
        }
        return companySearchResponse;
    }

    /**
     * get officer details
     *
     * @param apiKey               the api key
     * @param companyNumber        the company number
     * @param isActiveOfficersOnly the is active officers only
     * @return List<Officer> get officer details
     */
    private List<Officer> getOfficerDetails(String apiKey, String companyNumber, boolean isActiveOfficersOnly){

        Map<String, String> params = new HashMap<>();
        params.put(COMPANY_NUMBER, companyNumber);

        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.put(X_API_KEY, apiKey);

        String searchOfficerUrl = searchCompanyBaseUrl.concat(OFFICERS);

        List<Officer> officers = new ArrayList<>();

        try {
            OfficerResponse officerResponse = (OfficerResponse) apiService
                    .httpRequest(HttpMethod.GET, searchOfficerUrl, params, headers,
                            null, MediaType.APPLICATION_JSON, OfficerResponse.class, TIMEOUT);
            officerResponse.getItems().stream()
                    .filter(officerItem -> isActiveOfficersOnly ? StringUtils.isBlank(officerItem.getResignedOn()) ? true : false : true)
                    .forEach(officerItem->{
                        Officer officer = Officer
                                .builder()
                                .address(officerItem.getAddress())
                                .officerRole(officerItem.getOfficerRole())
                                .resignedOn(officerItem.resignedOn)
                                .appointedOn(officerItem.appointedOn)
                                .name(officerItem.name)
                                .build();
                        officers.add(officer);
                    });
        } catch (Exception e) {
            log.error("Exception occurred while fetching officer details with  Company Number : {} ", companyNumber);
        }
        return officers;
    }

    /**
     * get company details
     *
     * @param apiKey               the api key
     * @param companySearchRequest the company search request
     * @param isActiveCompanyOnly  the is active company only
     * @return CompanySearchResponse get company details
     * @throws CompanySearchCustomException CompanySearchCustomException
     */
    private CompanySearchResponse getCompanyDetails(String apiKey, CompanySearchRequest companySearchRequest, boolean isActiveCompanyOnly) throws CompanySearchCustomException {

        String searchParam = StringUtils.isNotBlank(companySearchRequest.getCompanyNumber()) ?
                companySearchRequest.getCompanyNumber() :
                companySearchRequest.getCompanyName();

        Map<String, String> params = new HashMap<>();
        params.put(QUERY, searchParam);

        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.put(X_API_KEY, apiKey);

        String searchCompanyUrl = searchCompanyBaseUrl.concat(SEARCH);

        CompanySearchResponse companySearchResponse;
        try{

            companySearchResponse = (CompanySearchResponse) apiService
                    .httpRequest(HttpMethod.GET, searchCompanyUrl, params, headers,
                            null, MediaType.APPLICATION_JSON, CompanySearchResponse.class, TIMEOUT);
            if(Objects.isNull(companySearchResponse) || CollectionUtils.isEmpty(companySearchResponse.getItems())){
                return null;
            }
            companySearchResponse.setItems(companySearchResponse.getItems().stream().
                    filter(company-> isActiveCompanyOnly ? (StringUtils.equalsIgnoreCase(company.getCompanyStatus(), ACTIVE) ? true : false) : true )
                    .collect(Collectors.toList()));

        } catch (Exception e) {
            log.error("Exception occurred while fetching company details with Company Name : {}, Company Number : {} ",
                    companySearchRequest.getCompanyName(), companySearchRequest.getCompanyNumber());
            throw CompanySearchCustomException.builder().backendMessage(e.getMessage()).build();
        }

        return companySearchResponse;
    }

}
