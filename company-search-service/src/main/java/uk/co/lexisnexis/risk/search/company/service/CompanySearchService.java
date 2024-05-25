package uk.co.lexisnexis.risk.search.company.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.lexisnexis.risk.search.company.dto.CompanySearchRequest;
import uk.co.lexisnexis.risk.search.company.dto.CompanySearchResponse;
import uk.co.lexisnexis.risk.search.company.entity.Company;
import uk.co.lexisnexis.risk.search.company.exception.CompanySearchCustomException;
import uk.co.lexisnexis.risk.search.company.mapper.CompanySearchMapper;
import uk.co.lexisnexis.risk.search.company.repository.CompanyRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * company search service
 *
 * @author ravin
 * @date 2024/05/25
 */
@Service
@Slf4j
public class CompanySearchService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TruProxyService truProxyService;

    @Autowired
    private CompanySearchDalService companySearchDalService;

    @Autowired
    private CompanySearchMapper companySearchMapper;

    /**
     * search companies
     *
     * @param apiKey               the api key
     * @param companySearchRequest the company search request
     * @param isActiveCompanyOnly  the is active company only
     * @param isActiveOfficersOnly the is active officers only
     * @param isLatestDataRequired the is latest data required
     * @return Optional<CompanySearchResponse> search companies
     * @throws CompanySearchCustomException CompanySearchCustomException
     */
    public Optional<CompanySearchResponse> searchCompanies(String apiKey,
                                                         uk.co.lexisnexis.risk.search.company.dto.CompanySearchRequest companySearchRequest,
                                                         boolean isActiveCompanyOnly, boolean isActiveOfficersOnly, boolean isLatestDataRequired)
            throws CompanySearchCustomException{

        Optional<CompanySearchResponse> companySearchResponse;
        if(isLatestDataRequired){
            companySearchResponse = getLatestCompanySearchData(apiKey, companySearchRequest, isActiveCompanyOnly, isActiveOfficersOnly);
        } else {
            companySearchResponse = getCompanySearchDataFromDatabase(apiKey, companySearchRequest, isActiveCompanyOnly, isActiveOfficersOnly);
        }
        return companySearchResponse;
    }

    /**
     * get company search data from database
     *
     * @param apiKey               the api key
     * @param companySearchRequest the company search request
     * @param isActiveCompanyOnly  the is active company only
     * @param isActiveOfficersOnly the is active officers only
     * @return Optional<CompanySearchResponse> get company search data from database
     * @throws CompanySearchCustomException CompanySearchCustomException
     */
    private Optional<CompanySearchResponse> getCompanySearchDataFromDatabase(String apiKey, CompanySearchRequest companySearchRequest, boolean isActiveCompanyOnly, boolean isActiveOfficersOnly) throws CompanySearchCustomException {
        Optional<CompanySearchResponse> companySearchResponse;
        List<Company> companies = getCompaniesByCriteria(companySearchRequest);
        if(CollectionUtils.isNotEmpty(companies)){
            companySearchResponse = prepareCompanySearchResponse(companies);
        } else {
            companySearchResponse = searchCompanies(apiKey, companySearchRequest, isActiveCompanyOnly, isActiveOfficersOnly, true);
        }
        return companySearchResponse;
    }

    /**
     * get latest company search data
     *
     * @param apiKey               the api key
     * @param companySearchRequest the company search request
     * @param isActiveCompanyOnly  the is active company only
     * @param isActiveOfficersOnly the is active officers only
     * @return Optional<CompanySearchResponse> get latest company search data
     * @throws CompanySearchCustomException CompanySearchCustomException
     */
    private Optional<CompanySearchResponse> getLatestCompanySearchData(String apiKey, CompanySearchRequest companySearchRequest, boolean isActiveCompanyOnly, boolean isActiveOfficersOnly) throws CompanySearchCustomException {
        Optional<CompanySearchResponse> companySearchResponse;
        companySearchResponse = truProxyService.searchCompanies(apiKey, companySearchRequest, isActiveCompanyOnly, isActiveOfficersOnly);
        companySearchDalService.saveCompanies(companySearchResponse);
        return companySearchResponse;
    }

    /**
     * get companies by criteria
     *
     * @param companySearchRequest the company search request
     * @return List<Company> get companies by criteria
     */
    private List<Company> getCompaniesByCriteria(CompanySearchRequest companySearchRequest) {
        List<Company> companies;
        if(StringUtils.isNotBlank(companySearchRequest.getCompanyNumber())){
            companies = companySearchDalService.getCompanyByNumbers(Collections.singletonList(companySearchRequest.getCompanyNumber()));
        } else {
            companies = companySearchDalService.getCompanyByName(companySearchRequest.getCompanyName());
        }
        return companies;
    }

    /**
     * prepare company search response
     *
     * @param companies the companies
     * @return Optional<CompanySearchResponse> prepare company search response
     */
    private Optional<CompanySearchResponse> prepareCompanySearchResponse(List<Company> companies) {

        CompanySearchResponse companySearchResponse = CompanySearchResponse
                .builder()
                .totalResults(companies.size())
                .items(companies.stream().map(
                        company -> companySearchMapper.mapCompanyModelToDto(company.getCompanyDetails())
                ).collect(Collectors.toList()))
                .build();
        return Optional.ofNullable(companySearchResponse);
    }

}
